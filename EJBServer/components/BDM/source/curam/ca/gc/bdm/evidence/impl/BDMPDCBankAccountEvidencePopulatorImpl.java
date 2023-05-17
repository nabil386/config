package curam.ca.gc.bdm.evidence.impl;

import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.core.sl.struct.CaseIDKey;
import curam.core.struct.BankAccountDtls;
import curam.core.struct.ConcernRoleBankAccountDtls;
import curam.core.struct.ConcernRoleKey;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.pdc.impl.PDCBankAccountEvidencePopulator;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.StringHelper;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TASK 16973 - Bank account evidence
 *
 * The
 * {@link {@link #populate(ConcernRoleKey, CaseIDKey, ConcernRoleBankAccountDtls, BankAccountDtls, DynamicEvidenceDataDetails)}
 * method of this implementation of {@link PDCBankAccountEvidencePopulator}
 * will populate the financial institution number and bank transit number
 * attributes of the {@link DynamicEvidenceDataDetails} if and only if the
 * following conditions are true.
 *
 * <ul>
 * <li>Both {@link BankAccountDtls} and {@link DynamicEvidenceDataDetails} are
 * not null.</li>
 * <li>Both the associated Evidence Type and Evidence Type Version can be
 * retrieved from the {@link DynamicEvidenceDataDetails} and have non-null
 * values.</li>
 * <li>The associated Evidence Type has a type code of
 * {@link PDCConst.PDCBANKACCOUNT}</li>
 * <li>The ETV declares "financialInstitutionNumber" and "bankTransitNumber"
 * data attributes.</li>
 * </li>
 * </ul>
 *
 * If these conditions hold the financial institution number and bank transit
 * number attributes will be set to empty
 * strings if {@link BankAccountDtls.bankSortCode} is not an eight character
 * string.
 * If the {@link BankAccountDtls.bankSortCode} is an eight character string, the
 * "financial institution number" attribute of the
 * {@link DynamicEvidenceDataDetails}
 * will be set to the first three characters and the "bank transit number" will
 * be set to the last five characters.
 *
 */
public class BDMPDCBankAccountEvidencePopulatorImpl
  implements PDCBankAccountEvidencePopulator {

  public static final Pattern BDM_BANK_SORT_CODE_PATTERN =
    Pattern.compile("^(.{3})(.{5})$");

  public static final int FIN_MATCHING_GROUP = 1;

  public static final int TRANSIT_MATCHING_GROUP = 2;

  /*
   * This method populate the financial institution number and bank transit
   * number attributes of the {@link DynamicEvidenceDataDetails}.
   *
   * @param concernRoleKey concern role identifier
   *
   * @param CaseIDKey case identifier
   *
   * @param bankAccountDtls bank account details
   *
   * @param concernRoleBankAccountDtls concern role bank account
   *
   * @param dynamicEvidenceDataDetails evidence data details
   *
   * @throws AppException Generic Exception Signature
   *
   * @throws InformationalException Generic Exception Signature
   */
  @Override
  public void populate(final ConcernRoleKey concernRoleKey,
    final CaseIDKey caseIDKey,
    final ConcernRoleBankAccountDtls concernRoleBankAccountDtls,
    final BankAccountDtls bankAccountDtls,
    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails)
    throws AppException, InformationalException {

    // set the financial institution number and bank sort code
    if (bankAccountDtls != null && BDMEvidenceUtil
      .hasBDMBankAccountCustomizations(dynamicEvidenceDataDetails)) {

      final Matcher bankSortCodeMatcher =
        BDM_BANK_SORT_CODE_PATTERN.matcher(bankAccountDtls.bankSortCode);

      if (bankSortCodeMatcher.matches()) {
        final String finValue = bankSortCodeMatcher.group(FIN_MATCHING_GROUP);
        final String transitValue =
          bankSortCodeMatcher.group(TRANSIT_MATCHING_GROUP);

        BDMEvidenceUtil.setDataAttributeNoCheck(
          BDMEvidenceUtil.kFINANCIAL_INSTITUTION_NUMBER, finValue,
          dynamicEvidenceDataDetails);
        BDMEvidenceUtil.setDataAttributeNoCheck(
          BDMEvidenceUtil.kBRANCH_TRANSIT_NUMBER, transitValue,
          dynamicEvidenceDataDetails);

      } else {
        BDMEvidenceUtil.setDataAttributeNoCheck(
          BDMEvidenceUtil.kFINANCIAL_INSTITUTION_NUMBER,
          StringHelper.EMPTY_STRING, dynamicEvidenceDataDetails);
        BDMEvidenceUtil.setDataAttributeNoCheck(
          BDMEvidenceUtil.kBRANCH_TRANSIT_NUMBER, StringHelper.EMPTY_STRING,
          dynamicEvidenceDataDetails);
      }

      BDMEvidenceUtil.setDataAttribute(BDMEvidenceUtil.kSORT_CODE,
        StringHelper.EMPTY_STRING, dynamicEvidenceDataDetails);
    }

  }

}
