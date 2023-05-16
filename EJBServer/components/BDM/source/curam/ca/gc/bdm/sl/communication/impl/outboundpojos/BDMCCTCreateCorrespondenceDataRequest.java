package curam.ca.gc.bdm.sl.communication.impl.outboundpojos;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BDMCCTCreateCorrespondenceDataRequest {

  private String client_address;

  private String Client_name;

  private String date_of_birth;

  private String date_of_death;

  private String gender;

  private String client_id;

  private String corr_language;

  private String oas_region_code;

  private String cpp_region_code;

  private String client_SIN;

  private String client_phone_number;

  private String country_agreement_code;

  private String date_of_death_client_name;

  private String application_type;

  private String program_code;

  public BDMCCTCreateCorrespondenceDataRequest() {

  }

  public BDMCCTCreateCorrespondenceDataRequest(final String client_address,
    final String client_name, final String date_of_birth,
    final String date_of_death, final String gender, final String client_id,
    final String corr_language, final String oas_region_code,
    final String cpp_region_code, final String client_SIN,
    final String client_phone_number, final String country_agreement_code,
    final String date_of_death_client_name, final String application_type,
    final String program_code) {

    super();
    this.client_address = client_address;
    this.Client_name = client_name;
    this.date_of_birth = date_of_birth;
    this.date_of_death = date_of_death;
    this.gender = gender;
    this.client_id = client_id;
    this.corr_language = corr_language;
    this.oas_region_code = oas_region_code;
    this.cpp_region_code = cpp_region_code;
    this.client_SIN = client_SIN;
    this.client_phone_number = client_phone_number;
    this.country_agreement_code = country_agreement_code;
    this.date_of_death_client_name = date_of_death_client_name;
    this.application_type = application_type;
    this.program_code = program_code;
  }

  @XmlElement
  public String getClient_address() {

    return this.client_address;
  }

  public void setClient_address(final String client_address) {

    this.client_address = client_address;
  }

  @XmlElement
  public String getClient_name() {

    return this.Client_name;
  }

  public void setClient_name(final String client_name) {

    this.Client_name = client_name;
  }

  @XmlElement
  public String getDate_of_birth() {

    return this.date_of_birth;
  }

  public void setDate_of_birth(final String date_of_birth) {

    this.date_of_birth = date_of_birth;
  }

  @XmlElement
  public String getDate_of_death() {

    return this.date_of_death;
  }

  public void setDate_of_death(final String date_of_death) {

    this.date_of_death = date_of_death;
  }

  @XmlElement
  public String getGender() {

    return this.gender;
  }

  public void setGender(final String gender) {

    this.gender = gender;
  }

  @XmlElement
  public String getClient_id() {

    return this.client_id;
  }

  public void setClient_id(final String client_id) {

    this.client_id = client_id;
  }

  @XmlElement
  public String getCorr_language() {

    return this.corr_language;
  }

  public void setCorr_language(final String corr_language) {

    this.corr_language = corr_language;
  }

  @XmlElement
  public String getOas_region_code() {

    return this.oas_region_code;
  }

  public void setOas_region_code(final String oas_region_code) {

    this.oas_region_code = oas_region_code;
  }

  @XmlElement
  public String getCpp_region_code() {

    return this.cpp_region_code;
  }

  public void setCpp_region_code(final String cpp_region_code) {

    this.cpp_region_code = cpp_region_code;
  }

  @XmlElement
  public String getClient_SIN() {

    return this.client_SIN;
  }

  public void setClient_SIN(final String client_SIN) {

    this.client_SIN = client_SIN;
  }

  @XmlElement
  public String getClient_phone_number() {

    return this.client_phone_number;
  }

  public void setClient_phone_number(final String client_phone_number) {

    this.client_phone_number = client_phone_number;
  }

  @XmlElement
  public String getCountry_agreement_code() {

    return this.country_agreement_code;
  }

  public void setCountry_agreement_code(final String country_agreement_code) {

    this.country_agreement_code = country_agreement_code;
  }

  @XmlElement
  public String getDate_of_death_client_name() {

    return this.date_of_death_client_name;
  }

  public void
    setDate_of_death_client_name(final String date_of_death_client_name) {

    this.date_of_death_client_name = date_of_death_client_name;
  }

  @XmlElement
  public String getApplication_type() {

    return this.application_type;
  }

  public void setApplication_type(final String application_type) {

    this.application_type = application_type;
  }

  @XmlElement
  public String getProgram_code() {

    return this.program_code;
  }

  public void setProgram_code(final String program_code) {

    this.program_code = program_code;
  }

}
