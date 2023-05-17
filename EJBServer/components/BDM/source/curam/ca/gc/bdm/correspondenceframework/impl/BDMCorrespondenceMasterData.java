
package curam.ca.gc.bdm.correspondenceframework.impl;

public class BDMCorrespondenceMasterData
  extends curam.util.type.struct.Struct<BDMCorrespondenceMasterData> {

  /**
   *
   */
  public final long serialVersionUID = 1L;

  public String programCode = "";

  public String programName = "";

  public String primaryalternateid = "";

  public long concernRoleID = 0;

  public String benefitAbbreviationText = "";

  public String BenefitCountryCode = "";

  public String benefitName = "";

  public String benefitApplicationCountryCode = "";

  public String benefitApplicationCountryName = "";

  public String countryOfAgreementCode = "";

  public curam.util.type.Date dateOfBirth = curam.util.type.Date.kZeroDate;

  public curam.util.type.Money amount = curam.util.type.Money.kZeroMoney;

  public int number = 0;

  public String clientIDText = "";

  public String unboundedText = "";

  public long caseID = 0;

  public String concernRoleName = "";

  public curam.util.type.Money entitlementOASCurrentAmount = null;

  public curam.util.type.Money entitlementGISCurrentAmount = null;

  public curam.util.type.Date applicationRecievedDate =
    curam.util.type.Date.kZeroDate;

}
