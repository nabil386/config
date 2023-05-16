package curam.ca.gc.bdm.citizen.datahub.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.codetable.BDMBANKACCOUNTCHOICE;
import curam.ca.gc.bdm.entity.financial.struct.BDMSetDestinationByLifeEventKey;
import curam.ca.gc.bdm.evidence.impl.BDMBankAccountValidation;
import curam.ca.gc.bdm.lifeevent.impl.BDMBankEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMBankEvidenceVO;
import curam.ca.gc.bdm.sl.financial.maintainpaymentdestination.intf.BDMMaintainPaymentDestination;
import curam.citizen.datahub.impl.CustomUpdateProcessor;
import curam.citizen.datahub.impl.DifferenceCommand;
import curam.codetable.BANKACCOUNTSTATUS;
import curam.codetable.BANKACCOUNTTYPE;
import curam.codetable.DESTINATIONTYPECODE;
import curam.codetable.EVIDENCECHANGEREASON;
import curam.codetable.METHODOFDELIVERY;
import curam.common.util.xml.dom.Document;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.struct.ReturnEvidenceDetails;
import curam.core.struct.ConcernRoleBankAccountKey;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.DatastoreFactory;
import curam.datastore.impl.Entity;
import curam.datastore.impl.NoSuchSchemaException;
import curam.participant.impl.ConcernRoleDAO;
import curam.pdc.impl.PDCConst;
import curam.piwrapper.caseheader.impl.CaseHeaderDAO;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.StringUtil;
import curam.util.type.Date;
import curam.workspaceservices.codetable.impl.DataHubUpdateProcessorTypeEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BDMLifeEventPaymentUpdateProcessor
  implements CustomUpdateProcessor {

  @Inject
  BDMBankEvidence bdmBankEvidence;

  @Inject
  private BDMBankAccountValidation bankValidation;

  @Inject
  ConcernRoleDAO concernRoleDAO;

  @Inject
  CaseHeaderDAO caseHeaderDAO;

  @Inject
  BDMMaintainPaymentDestination maintainPaymentDestination;

  protected BDMLifeEventPaymentUpdateProcessor() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Override
  public void processUpdate(
    final DataHubUpdateProcessorTypeEntry dataHubUpdateProcessorType,
    final String context, final Entity entity,
    final DifferenceCommand diffCommand) {

    final Document datastoreDifferenceXML = diffCommand.convertToXML();

    try {
      updatePaymentInformation(entity, datastoreDifferenceXML);
    } catch (AppException | InformationalException
      | NoSuchSchemaException e) {
      e.printStackTrace();
    }
  }

  /**
   *
   * @param rootEntity
   * @param datastoreDifferenceXML
   * @throws AppException
   * @throws InformationalException
   * @throws NoSuchSchemaException
   */
  private void updatePaymentInformation(final Entity rootEntity,
    final Document datastoreDifferenceXML)
    throws AppException, InformationalException, NoSuchSchemaException {

    final Datastore datastore = DatastoreFactory.newInstance()
      .openDatastore(BDMLifeEventDatastoreConstants.BDM_LIFE_EVENT_SCHEMA);
    final Entity personEntity = rootEntity.getChildEntities(
      datastore.getEntityType(BDMLifeEventDatastoreConstants.PERSON))[0];
    // Assuming only 1 payment Entity
    final Entity paymentEntity = personEntity.getChildEntities(
      datastore.getEntityType(BDMLifeEventDatastoreConstants.PAYMENT))[0];

    final Entity[] paymentBanksEntities = personEntity.getChildEntities(
      datastore.getEntityType(BDMLifeEventDatastoreConstants.PAYMENT_BANKS));

    final BDMLifeEventCustomProcessorUtil bdmLifeEventCustomProcessorUtil =
      new BDMLifeEventCustomProcessorUtil();

    final Long concernRoleID = Long.parseLong(
      personEntity.getAttribute(BDMLifeEventDatastoreConstants.LOCAL_ID));

    // get the programTypeCode
    final String programTypeCode = bdmLifeEventCustomProcessorUtil
      .getSelectedBenefitProgramType(rootEntity);

    // check if cheque or bank account is chosen
    if (!programTypeCode.isEmpty() && !StringUtil.isNullOrEmpty(paymentEntity
      .getAttribute(BDMLifeEventDatastoreConstants.PAYMENT_METHOD))) {
      final String preferredPaymentMethod = paymentEntity
        .getAttribute(BDMLifeEventDatastoreConstants.PAYMENT_METHOD);
      if (preferredPaymentMethod.equals(METHODOFDELIVERY.CHEQUE)) {
        // set payment destination
        final BDMSetDestinationByLifeEventKey lifeEventKey =
          new BDMSetDestinationByLifeEventKey();
        lifeEventKey.concernRoleBankAccountID = 0L;
        lifeEventKey.concernRoleID = concernRoleID;
        lifeEventKey.destinationType = DESTINATIONTYPECODE.ADDRESS;
        lifeEventKey.programType = programTypeCode;
        lifeEventKey.startDate = Date.getCurrentDate();
        maintainPaymentDestination.setDestinationByLifeEvent(lifeEventKey);

      } else if (preferredPaymentMethod.equals(METHODOFDELIVERY.EFT)) {
        // Get new payment entity for new bank from datastore
        final String preferredBankAccount = paymentEntity.getAttribute(
          BDMLifeEventDatastoreConstants.PREFERRED_BANK_ACCOUNT);
        long preferredBankEvidenceID = 0L;
        if (StringUtil.isNullOrEmpty(preferredBankAccount)
          || preferredBankAccount.equals(BDMBANKACCOUNTCHOICE.ADD_NEW)) {
          final List<BDMBankEvidenceVO> bdmBankEvidenceVOList =
            new ArrayList<BDMBankEvidenceVO>();
          // Set payment from datastore
          final String bankAccountName = paymentEntity
            .getAttribute(BDMLifeEventDatastoreConstants.BANK_ACCOUNT_NAME);
          final String bankBranchNum = paymentEntity
            .getAttribute(BDMLifeEventDatastoreConstants.BANK_BRANCH_NUM);
          final String bankInstNum = paymentEntity.getAttribute(
            BDMLifeEventDatastoreConstants.BANK_INSTITUTION_CODE);
          final String accountNumber = paymentEntity
            .getAttribute(BDMLifeEventDatastoreConstants.BANK_ACCT_NUM);
          final String sortCode = paymentEntity
            .getAttribute(BDMLifeEventDatastoreConstants.BANK_SORT_CODE);
          // Process payment update
          final BDMBankEvidenceVO bdmBankEvidenceVO = new BDMBankEvidenceVO();
          bdmBankEvidenceVO.setAccountName(bankAccountName);
          bdmBankEvidenceVO.setBranchTransitNumber(bankBranchNum);
          bdmBankEvidenceVO.setFinancialInstitutionNumber(bankInstNum);
          bdmBankEvidenceVO.setAccountNumber(accountNumber);
          bdmBankEvidenceVO.setSortCode(sortCode);
          bdmBankEvidenceVO.setPreferredInd(true);
          bdmBankEvidenceVO.setAccountStatus(BANKACCOUNTSTATUS.OPEN);
          bdmBankEvidenceVO.setAccountType(BANKACCOUNTTYPE.PERSONALCURRENT);
          bdmBankEvidenceVO.setFromDate(Date.getCurrentDate());
          bdmBankEvidenceVO.setJointAccountInd(false);
          bdmBankEvidenceVO.setEvidenceID(0L);
          bdmBankEvidenceVOList.add(bdmBankEvidenceVO);
          final ReturnEvidenceDetails returnEvidenceDetails = bdmBankEvidence
            .createBankEvidence(concernRoleID, bdmBankEvidenceVO,
              EVIDENCECHANGEREASON.REPORTED_BYCLIENT_LIFE_EVENT);
          if (!Objects.isNull(returnEvidenceDetails)) {
            preferredBankEvidenceID =
              returnEvidenceDetails.evidenceKey.evidenceID;
          }
        } else if (preferredBankAccount
          .equals(BDMBANKACCOUNTCHOICE.USE_EXISTING)) {

          for (final Entity paymentBanksEntity : paymentBanksEntities) {
            if (!StringUtil.isNullOrEmpty(paymentBanksEntity.getAttribute(
              BDMLifeEventDatastoreConstants.PAYMENT_PREFERRED_BANK_FOR_PROGRAM))) {
              final boolean preferredBankForProgram =
                Boolean.parseBoolean(paymentBanksEntity.getAttribute(
                  BDMLifeEventDatastoreConstants.PAYMENT_PREFERRED_BANK_FOR_PROGRAM));
              if (preferredBankForProgram) {
                preferredBankEvidenceID =
                  Long.parseLong(paymentBanksEntity.getAttribute(
                    BDMLifeEventDatastoreConstants.PAYMENT_BANK_EVIDENCE_ID));
                break;
              }
            }
          }
        }
        // set payment destination
        if (preferredBankEvidenceID != 0) {
          // retrieve concernroleBankAccountID
          final EIEvidenceKey evidenceKey = new EIEvidenceKey();
          evidenceKey.evidenceID = preferredBankEvidenceID;
          evidenceKey.evidenceType = PDCConst.PDCBANKACCOUNT;
          final ConcernRoleBankAccountKey concernRoleBankAccountKey =
            bankValidation.getConcernRoleBankAccountKey(evidenceKey);
          // invoke api to set payment destination for life event
          final BDMSetDestinationByLifeEventKey lifeEventKey =
            new BDMSetDestinationByLifeEventKey();
          lifeEventKey.concernRoleBankAccountID =
            concernRoleBankAccountKey.concernRoleBankAccountID;
          lifeEventKey.concernRoleID = concernRoleID;
          lifeEventKey.destinationType = DESTINATIONTYPECODE.BANKACCOUNT;
          lifeEventKey.programType = programTypeCode;
          lifeEventKey.startDate = Date.getCurrentDate();
          maintainPaymentDestination.setDestinationByLifeEvent(lifeEventKey);
        }
      }
    }
  }

}
