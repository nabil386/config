package curam.ca.gc.bdmoas.evidence.events.impl;

import curam.ca.gc.bdm.evidence.events.impl.BDMAbstractEvidenceEventHandler;
import curam.ca.gc.bdm.message.BDMEVIDENCE;
import curam.ca.gc.bdm.vtw.deduction.entity.fact.BDMVTWDeductionFactory;
import curam.ca.gc.bdm.vtw.deduction.entity.struct.BDMVTWDeductionCountDetails;
import curam.ca.gc.bdm.vtw.deduction.entity.struct.BDMVTWDeductionCountKey;
import curam.ca.gc.bdm.vtw.deduction.entity.struct.BDMVTWDeductionKeyStruct1;
import curam.ca.gc.bdmoas.entity.paymentschedule.fact.BDMOASAnnualPaymentScheduleFactory;
import curam.ca.gc.bdmoas.entity.paymentschedule.struct.BDMOASAnnualPaymentScheduleDtls;
import curam.ca.gc.bdmoas.entity.paymentschedule.struct.BDMOASAnnualPaymentScheduleMonthAndYearDetails;
import curam.codetable.RECORDSTATUS;
import curam.codetable.impl.CASEEVIDENCEEntry;
import curam.core.impl.CuramConst;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorModifyDtls;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.StringUtil;
import curam.util.type.Date;
import curam.util.type.NotFoundIndicator;
import java.util.Calendar;

public class BDMOASVoluntaryTaxWithholdEventHandler
  extends BDMAbstractEvidenceEventHandler {

  /**
   * Constructor
   */
  public BDMOASVoluntaryTaxWithholdEventHandler() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Override
  public CASEEVIDENCEEntry evidenceType() {

    return CASEEVIDENCEEntry.BDMVTW;
  }

  /**
   * Subscribe the PreModify event
   */
  @Override
  public boolean subscribePreModify() {

    return true;
  }

  /**
   * Evidence Pre Modify Handler
   */
  @Override
  public void preModify(final EIEvidenceKey key,
    final EvidenceDescriptorModifyDtls descriptor,
    final Object evidenceObject, final EIEvidenceKey parentKey)
    throws AppException, InformationalException {

    // Check if the VTW evidence is already considered for the
    // processing.
    final NotFoundIndicator notFoundIndicator = new NotFoundIndicator();
    final BDMVTWDeductionKeyStruct1 bdmVTWDeductionKeyStruct1 =
      new BDMVTWDeductionKeyStruct1();
    bdmVTWDeductionKeyStruct1.evidenceID = key.evidenceID;
    bdmVTWDeductionKeyStruct1.recordStatusCode = RECORDSTATUS.NORMAL;
    BDMVTWDeductionFactory.newInstance().readByEvidenceID(notFoundIndicator,
      bdmVTWDeductionKeyStruct1);

    if (!notFoundIndicator.isNotFound()) {

      final BDMVTWDeductionCountKey bdmVTWDeductionCountKey =
        new BDMVTWDeductionCountKey();
      bdmVTWDeductionCountKey.evidenceID = key.evidenceID;

      final BDMVTWDeductionCountDetails bdmVTWDeductionCountDetails =
        BDMVTWDeductionFactory.newInstance()
          .countVTWILIByEvidenceID(bdmVTWDeductionCountKey);

      final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
        (DynamicEvidenceDataDetails) evidenceObject;

      if (bdmVTWDeductionCountDetails.numberOfRecords > CuramConst.gkZero
        && !StringUtil.isNullOrEmpty(
          dynamicEvidenceDataDetails.getAttribute("endDate").getValue())) {

        final Date endDate = Date.getDate(
          dynamicEvidenceDataDetails.getAttribute("endDate").getValue());

        // Get the current date
        final Date currentDate = Date.getCurrentDate();

        // Get the current date month and year and pass to get the current
        // month issue date
        final BDMOASAnnualPaymentScheduleMonthAndYearDetails bdmoasAnnualPaymentScheduleMonthAndYearDetails =
          new BDMOASAnnualPaymentScheduleMonthAndYearDetails();
        bdmoasAnnualPaymentScheduleMonthAndYearDetails.month =
          currentDate.getCalendar().get(Calendar.MONTH) + 1;
        bdmoasAnnualPaymentScheduleMonthAndYearDetails.year =
          currentDate.getCalendar().get(Calendar.YEAR);

        final NotFoundIndicator notFoundIndicatorAnnualSchedule =
          new NotFoundIndicator();

        final BDMOASAnnualPaymentScheduleDtls bdmOASAnnualPaymentScheduleDtls =
          BDMOASAnnualPaymentScheduleFactory.newInstance().readByMonthAndYear(
            notFoundIndicatorAnnualSchedule,
            bdmoasAnnualPaymentScheduleMonthAndYearDetails);

        if (!notFoundIndicatorAnnualSchedule.isNotFound()
          && (endDate.before(bdmOASAnnualPaymentScheduleDtls.paymentDueDate)
            || endDate
              .equals(bdmOASAnnualPaymentScheduleDtls.paymentDueDate))) {

          throw new AppException(
            BDMEVIDENCE.ERR_END_DATE_MODIFY_VTW_EVIDENCE);
        }
      }
    }
  }

}
