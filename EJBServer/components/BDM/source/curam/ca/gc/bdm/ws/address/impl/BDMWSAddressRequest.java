
package curam.ca.gc.bdm.ws.address.impl;

import curam.core.impl.CuramConst;
import org.apache.commons.lang.StringUtils;

/**
 * A POJO class to hold the WS Address request object.
 *
 * This POJO is instantiated by the consumer of the BDMWSAddress service
 * ({@linkplain BDMWSAddressService} and is used as an input parameter to the
 * service API
 */
public class BDMWSAddressRequest {

  /**
   * The language enum options.
   *
   * NOTE: Depending on requirements, this has potential to be abstracted to a
   * codetable if runtime modification/extension is required.
   */
  public enum Language {
    ENGLISH {

      @Override
      public String toString() {

        return BDMWSAddressConstants.kEnglish;
      }
    },
    FRENCH {

      @Override
      public String toString() {

        return BDMWSAddressConstants.kFrench;
      }
    },
  }

  /**
   * Address line
   */
  private String addressLine = "";

  /**
   * City
   */
  private String city = "";

  /**
   * Province.
   *
   * NOTE: We do not have exact steering on these codes but they look to be the
   * internationally approved postal and ISO abbreviations, e.g. Quebec = QC.
   * FIXME: This may be better represented as an enumerated datatype retrieved
   * from a codetable
   */
  private String province = "";

  /**
   * Postal Code.
   */
  private String postalCode = "";

  /**
   * Country
   *
   * NOTE: We do not have exact steering on these codes but they are likely the
   * ISO Alpha 3 country codes, e.g. Canada = CAN
   */
  private String country = "";

  /**
   * The language option for the results of the web service.
   *
   * Default to English.
   */
  private Language language = Language.ENGLISH;

  /**
   * Enables B64 encoding at field level
   */
  private boolean enableB64EncodingOnFields = false;

  // ---------------------------------------------------------

  public String getAddressLine() {

    return this.addressLine;
  }

  public void setAddressLine(final String addressLine) {

    this.addressLine = addressLine;
  }

  public String getCity() {

    return this.city;
  }

  public void setCity(final String city) {

    this.city = city;
  }

  public String getProvince() {

    return this.province;
  }

  public void setProvince(final String province) {

    this.province = province;
  }

  public String getPostalCode() {

    return this.postalCode;
  }

  public void setPostalCode(final String postalCode) {

    this.postalCode =
      StringUtils.replace(postalCode, CuramConst.gkSpace, "").toUpperCase();
  }

  public String getCountry() {

    return this.country;
  }

  public void setCountry(final String country) {

    this.country = country;
  }

  public Language getLanguage() {

    return this.language;
  }

  public void setLanguage(final Language language) {

    this.language = language;
  }

  public boolean isB64EncodingEnabledOnFields() {

    return this.enableB64EncodingOnFields;
  }

  public void
    setEnableB64EncodingOnFields(final boolean enableB64EncodingOnFields) {

    this.enableB64EncodingOnFields = enableB64EncodingOnFields;
  }

}
