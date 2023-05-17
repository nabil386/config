package curam.ca.gc.bdm.citizen.datahub.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.codetable.BDMBENEFITPROGRAMTYPE;
import curam.ca.gc.bdm.entity.financial.fact.BDMCodeTableComboFactory;
import curam.ca.gc.bdm.entity.financial.fact.BDMPaymentDestinationFactory;
import curam.ca.gc.bdm.entity.financial.intf.BDMCodeTableCombo;
import curam.ca.gc.bdm.entity.financial.intf.BDMPaymentDestination;
import curam.ca.gc.bdm.entity.financial.struct.BDMReadGovernCodeBySubOrdTableDetails;
import curam.ca.gc.bdm.entity.financial.struct.BDMReadGovernCodeBySubOrdTableKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMSearchEFTDestinationDetails;
import curam.ca.gc.bdm.entity.financial.struct.BDMSearchEFTDestinationDetailsList;
import curam.ca.gc.bdm.entity.financial.struct.BDMSearchEFTDestinationKey;
import curam.ca.gc.bdm.lifeevent.impl.BDMBankEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMBankEvidenceVO;
import curam.citizen.datahub.impl.Citizen;
import curam.citizen.datahub.impl.CustomViewProcessor;
import curam.citizenworkspace.message.impl.CITIZENDATAHUBExceptionCreator;
import curam.codetable.CASESTATUS;
import curam.codetable.DESTINATIONTYPECODE;
import curam.codetable.METHODOFDELIVERY;
import curam.codetable.PRODUCTCATEGORY;
import curam.codetable.RECORDSTATUS;
import curam.codetable.impl.CASESTATUSEntry;
import curam.codetable.impl.CASETYPECODEEntry;
import curam.codetable.impl.CONCERNROLETYPEEntry;
import curam.common.util.xml.dom.DOMException;
import curam.common.util.xml.dom.Document;
import curam.common.util.xml.dom.Element;
import curam.common.util.xml.dom.xpath.XPath;
import curam.core.facade.fact.IntegratedCaseFactory;
import curam.core.facade.intf.IntegratedCase;
import curam.core.facade.struct.ICProductDeliveryDetails;
import curam.core.facade.struct.IntegratedCaseIDKey;
import curam.core.facade.struct.ListICProductDeliveryDetails;
import curam.core.fact.ConcernRoleBankAccountFactory;
import curam.core.intf.ConcernRoleBankAccount;
import curam.core.struct.BankAccountRMDtls;
import curam.core.struct.BankAccountRMDtlsList;
import curam.core.struct.ConcernRoleIDStatusCodeKey;
import curam.participant.impl.ConcernRole;
import curam.participant.impl.ConcernRoleDAO;
import curam.participant.person.impl.Person;
import curam.participant.person.impl.PersonDAO;
import curam.piwrapper.caseheader.impl.CaseHeaderDAO;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.type.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BDMLifeEventPaymentViewProcessor implements CustomViewProcessor {

  @Inject
  PersonDAO personDAO;

  @Inject
  ConcernRoleDAO concernRoleDAO;

  @Inject
  CaseHeaderDAO caseHeaderDAO;

  @Inject
  BDMBankEvidence bdmBankEvidence;

  protected BDMLifeEventPaymentViewProcessor() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Override
  public void processView(final Citizen citizen, final String context,
    final Document citizenData) throws AppException, InformationalException {

    final Element person = getPrimaryNode(citizenData, citizen);
  }

  private Element getPrimaryNode(final Document citizenData,
    final Citizen citizen) throws AppException, InformationalException {

    Element retval = null;
    boolean foundPrimary = false;
    try {
      final XPath handlerExpression =
        new XPath("//Person[@isPrimaryParticipant='true']");
      final List nodes = handlerExpression.selectNodes(citizenData);
      if (!nodes.isEmpty()) {
        foundPrimary = true;
        retval = (Element) nodes.get(0);
      }
    } catch (final DOMException e) {
      final AppException err =
        CITIZENDATAHUBExceptionCreator.ERR_EVIDENCE_READ_FAILURE(
          Long.valueOf(Long.parseLong(citizen.getIdentifier())));
      err.initCause(e);
      throw err;
    }
    if (foundPrimary) {
      retval = setPersonEntityAttributes(retval, citizenData, citizen);
    } else {
      retval = new Element("Person");
      retval = setPersonEntityAttributes(retval, citizenData, citizen);
    }
    return retval;
  }

  /**
   *
   * @param personEl
   * @param citizenData
   * @param citizen
   * @throws AppException
   * @throws InformationalException
   */
  private Element setPersonEntityAttributes(final Element personEl,
    final Document citizenData, final Citizen citizen)
    throws AppException, InformationalException {

    final ConcernRole primaryConcernRole = this.concernRoleDAO
      .get(Long.valueOf(Long.parseLong(citizen.getIdentifier())));
    personEl.setAttribute(
      BDMLifeEventDatastoreConstants.IS_PRIMARY_PARTICIPANT, "true");
    personEl.setAttribute(BDMLifeEventDatastoreConstants.LOCAL_ID,
      primaryConcernRole.getID().toString());
    if (!primaryConcernRole.getConcernRoleType()
      .equals(CONCERNROLETYPEEntry.PERSON))
      return personEl;
    final Long concernRoleID = primaryConcernRole.getID();
    final Person person = this.personDAO.get(primaryConcernRole.getID());
    personEl.setAttribute(BDMLifeEventDatastoreConstants.PERSON_ID,
      person.getID().toString());

    final IntegratedCase integratedcase = IntegratedCaseFactory.newInstance();
    IntegratedCaseIDKey integratedCaseIDKey = null;
    final BDMCodeTableCombo bdmCodeTableComboObj =
      BDMCodeTableComboFactory.newInstance();
    final ConcernRoleBankAccount concernRoleBankAccountObj =
      ConcernRoleBankAccountFactory.newInstance();

    final List<curam.piwrapper.caseheader.impl.CaseHeader> clientCaseList =
      this.caseHeaderDAO.searchByParticipant(primaryConcernRole);

    for (final curam.piwrapper.caseheader.impl.CaseHeader caseDetails : clientCaseList) {

      if (caseDetails.getCaseType().equals(CASETYPECODEEntry.INTEGRATEDCASE)
        && !caseDetails.getStatus().equals(CASESTATUSEntry.CLOSED)) {
        boolean existsActiveBenefts = false;
        integratedCaseIDKey = new IntegratedCaseIDKey();
        integratedCaseIDKey.caseID = caseDetails.getID();
        final ListICProductDeliveryDetails listICProductDeliveryDetails =
          integratedcase.listProduct(integratedCaseIDKey);
        for (final ICProductDeliveryDetails icProductDeliveryDetails : listICProductDeliveryDetails.dtls) {
          if (icProductDeliveryDetails.statusCode.equals(CASESTATUS.ACTIVE)) {
            existsActiveBenefts = true;
            break;
          }
        }
        /*
         * concreteReaderDAOs.put(CASETYPECODEEntry.ISSUE,
         * issueConfigurationDAO);
         * concreteReaderDAOs.put(CASETYPECODEEntry.SCREENINGCASE,
         * screeningConfigurationDAO);
         * concreteReaderDAOs.put(CASETYPECODEEntry.INTEGRATEDCASE,
         * adminIntegratedCaseDAO);
         * concreteReaderDAOs.put(CASETYPECODEEntry.PRODUCTDELIVERY,
         * benefitProductDAO);
         * concreteReaderDAOs.put(CASETYPECODEEntry.LIABILITY,
         * liabilityProductDAO);
         * concreteReaderDAOs.put(CASETYPECODEEntry.SERVICEPLAN,
         * servicePlanDAO);
         * concreteReaderDAOs.put(CASETYPECODEEntry.INVESTIGATIONCASE,
         * investigationConfigDAO);
         * concreteReaderDAOs.put(CASETYPECODEEntry.ASSESSMENTDELIVERY,
         * assessmentConfigurationDAO);
         */

        if (existsActiveBenefts) {
          personEl.setAttribute(
            BDMLifeEventDatastoreConstants.PERSON_ACTIVE_PROGRAMS, "true");
          final Element programsEntity =
            new Element(BDMLifeEventDatastoreConstants.PROGRAMS_ENTITY);
          programsEntity.setAttribute(
            BDMLifeEventDatastoreConstants.PROGRAM_CASE_ID,
            String.valueOf(caseDetails.getID())); // ((Long)caseDetails.getID()).longValue()
          programsEntity.setAttribute(
            BDMLifeEventDatastoreConstants.PROGRAMS_ACTIVE_PROGRAM_SELECTED,
            "false");
          programsEntity.setAttribute(
            BDMLifeEventDatastoreConstants.PROGRAMS_ACTIVE_PROGRAM_NAME,
            caseDetails.getAdminCaseConfiguration()
              .getCaseConfigurationName());
          programsEntity.setAttribute(
            BDMLifeEventDatastoreConstants.PROGRAM_PAYMENT_METHOD,
            METHODOFDELIVERY.CHEQUE);
          final BDMReadGovernCodeBySubOrdTableKey bdmReadGovernCodeBySubOrdTableKey =
            new BDMReadGovernCodeBySubOrdTableKey();
          bdmReadGovernCodeBySubOrdTableKey.governTableName =
            BDMBENEFITPROGRAMTYPE.TABLENAME;
          bdmReadGovernCodeBySubOrdTableKey.recordStatusCode =
            RECORDSTATUS.NORMAL;
          bdmReadGovernCodeBySubOrdTableKey.subOrdTableName =
            PRODUCTCATEGORY.TABLENAME;
          bdmReadGovernCodeBySubOrdTableKey.subOrdCode =
            caseDetails.getIntegratedCaseType().getCode();
          final BDMReadGovernCodeBySubOrdTableDetails bdmReadGovernCodeBySubOrdTableDetails =
            bdmCodeTableComboObj
              .readGovernCodeBySubOrdTable(bdmReadGovernCodeBySubOrdTableKey);
          // look for payment method and account info for this program by
          // the associated BDMPaymentDestination records
          final long concernRoleBankAccountID =
            getCurrentBankAccountIDForProgram(concernRoleID,
              bdmReadGovernCodeBySubOrdTableDetails.governCode);
          if (concernRoleBankAccountID != 0) {
            programsEntity.setAttribute(
              BDMLifeEventDatastoreConstants.PROGRAM_PAYMENT_METHOD,
              METHODOFDELIVERY.EFT);
            programsEntity.setAttribute(
              BDMLifeEventDatastoreConstants.PROGRAM_PAYMENT_BANK_ACCOUNT_ID,
              Long.toString(concernRoleBankAccountID));
            final ConcernRoleIDStatusCodeKey concernRoleIDStatusCodeKey =
              new ConcernRoleIDStatusCodeKey();
            concernRoleIDStatusCodeKey.concernRoleID = concernRoleID;
            concernRoleIDStatusCodeKey.statusCode = RECORDSTATUS.NORMAL;
            final BankAccountRMDtlsList bankAccountRMDtlsList =
              concernRoleBankAccountObj
                .searchByConcernRoleIDAndStatus(concernRoleIDStatusCodeKey);

            for (final BankAccountRMDtls bankAccountRMDtls : bankAccountRMDtlsList.dtls) {
              if (bankAccountRMDtls.concernRoleBankAccountID == concernRoleBankAccountID) {
                programsEntity.setAttribute(
                  BDMLifeEventDatastoreConstants.PROGRAM_PAYMENT_BANK_ACCOUNT_NUMBER,
                  bankAccountRMDtls.accountNumber);
                programsEntity.setAttribute(
                  BDMLifeEventDatastoreConstants.PROGRAM_PAYMENT_BANK_SORT_CODE,
                  bankAccountRMDtls.bankSortCode);
              }
            }
          }

          personEl.addContent(programsEntity);
        }
      }
    }

    // Get bank accounts from dyn evidence
    // Only displays open bank accounts that have an end date in the future or
    // no end date. If two accounts are the same then display the one that has
    // the latest startdate, then latest effective date/time.
    final HashMap<String, Integer> bankAccountMap =
      new HashMap<String, Integer>();

    final List<BDMBankEvidenceVO> bdmBankEvidenceVOList =
      bdmBankEvidence.getBankEvidenceValueObjectList(concernRoleID);
    if (!bdmBankEvidenceVOList.isEmpty()) {
      // first sweep: set up the hashmap
      for (int i = 0; i < bdmBankEvidenceVOList.size(); i++) {
        final BDMBankEvidenceVO bdmBankEvidenceVO =
          bdmBankEvidenceVOList.get(i);
        // Get payment from dyn evidence
        // paymentBanksEntity Entity
        if (bdmBankEvidenceVO.getToDate() == null
          || bdmBankEvidenceVO.getToDate().isZero()
          || bdmBankEvidenceVO.getToDate().after(Date.getCurrentDate())) {
          final String key = bdmBankEvidenceVO.getSortCode()
            + bdmBankEvidenceVO.getAccountNumber();
          if (bankAccountMap.containsKey(key)) {
            final Integer loc = bankAccountMap.get(key);
            if (!bdmBankEvidenceVOList.get(loc.intValue()).getFromDate()
              .after(bdmBankEvidenceVO.getFromDate())) {
              // replace with the bank account with latest startdate
              bankAccountMap.replace(key, i);
            }
          } else {
            bankAccountMap.put(key, i);
          }
        }
      }
      // second sweep: Set bank account info into datastore
      if (!bankAccountMap.isEmpty()) {
        personEl.setAttribute(
          BDMLifeEventDatastoreConstants.HAS_ACTIVE_BANK_ACCOUNTS, "true");
      }
      final Iterator hmIterator = bankAccountMap.entrySet().iterator();
      while (hmIterator.hasNext()) {
        final Map.Entry mapElement = (Map.Entry) hmIterator.next();
        final int i = (int) mapElement.getValue();
        final BDMBankEvidenceVO bdmBankEvidenceVO =
          bdmBankEvidenceVOList.get(i);
        final Element paymentBanksEntity =
          new Element(BDMLifeEventDatastoreConstants.PAYMENT_BANKS);
        final String bankAccountName = bdmBankEvidenceVO.getAccountName();
        final String accountNumber = bdmBankEvidenceVO.getAccountNumber();
        final Long bankEvidenceID = bdmBankEvidenceVO.getEvidenceID();
        // Set payment into datastore
        paymentBanksEntity.setAttribute(
          BDMLifeEventDatastoreConstants.BANK_ACCT_NUM, accountNumber);
        paymentBanksEntity.setAttribute(
          BDMLifeEventDatastoreConstants.BANK_BRANCH_NUM,
          bdmBankEvidenceVO.getBranchTransitNumber());
        paymentBanksEntity.setAttribute(
          BDMLifeEventDatastoreConstants.BANK_INSTITUTION_CODE,
          bdmBankEvidenceVO.getFinancialInstitutionNumber());
        paymentBanksEntity.setAttribute(
          BDMLifeEventDatastoreConstants.BANK_SORT_CODE,
          bdmBankEvidenceVO.getSortCode());
        paymentBanksEntity.setAttribute(
          BDMLifeEventDatastoreConstants.BANK_ACCOUNT_NAME_AND_NUMBER,
          bankAccountName + " (xxx"
            + accountNumber.substring(accountNumber.length() - 4) + ")");
        paymentBanksEntity.setAttribute(
          BDMLifeEventDatastoreConstants.PAYMENT_BANK_EVIDENCE_ID,
          bankEvidenceID.toString());
        personEl.addContent(paymentBanksEntity);
      }
    }
    return personEl;
  }

  private long getCurrentBankAccountIDForProgram(final long concernRoleID,
    final String programTypeCode)
    throws AppException, InformationalException {

    final BDMPaymentDestination bdmPaymentDestinationObj =
      BDMPaymentDestinationFactory.newInstance();
    final BDMSearchEFTDestinationKey searchEFTDestinationKey =
      new BDMSearchEFTDestinationKey();
    searchEFTDestinationKey.concernRoleID = concernRoleID;
    searchEFTDestinationKey.destinationType = DESTINATIONTYPECODE.BANKACCOUNT;
    searchEFTDestinationKey.recordStatusCode = RECORDSTATUS.NORMAL;
    final BDMSearchEFTDestinationDetailsList paymentDestinationList =
      bdmPaymentDestinationObj
        .searchEFTPaymentDestination(searchEFTDestinationKey);
    for (final BDMSearchEFTDestinationDetails paymentDestination : paymentDestinationList.dtls) {
      if (paymentDestination.programType.equals(programTypeCode)
        && !paymentDestination.startDate.after(Date.getCurrentDate())
        && paymentDestination.endDate.isZero()
        || paymentDestination.endDate.after(Date.getCurrentDate())) {
        return paymentDestination.destinationID;
      }
    }
    return 0;
  }

}
