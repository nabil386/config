
package curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl.validatepojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class SsFather {

  @SerializedName("nc:PersonGivenName")
  @Expose
  private String ncPersonGivenName;

  @SerializedName("nc:PersonSurName")
  @Expose
  private String ncPersonSurName;

  public String getNcPersonGivenName() {

    return ncPersonGivenName;
  }

  public void setNcPersonGivenName(final String ncPersonGivenName) {

    this.ncPersonGivenName = ncPersonGivenName;
  }

  public String getNcPersonSurName() {

    return ncPersonSurName;
  }

  public void setNcPersonSurName(final String ncPersonSurName) {

    this.ncPersonSurName = ncPersonSurName;
  }

}
