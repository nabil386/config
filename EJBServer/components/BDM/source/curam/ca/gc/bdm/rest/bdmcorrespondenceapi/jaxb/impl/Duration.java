//
// This file was generated by the Eclipse Implementation of JAXB, v3.0.1
// See https://eclipse-ee4j.github.io/jaxb-ri
// Any modifications to this file will be lost upon recompilation of the source
// schema.
// Generated on: 2022.10.17 at 01:30:31 PM EDT
//

package curam.ca.gc.bdm.rest.bdmcorrespondenceapi.jaxb.impl;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;

/**
 * A data type for a duration of time with the format PnYnMnDTnHnMnS, where nY
 * is the number of years, nM is the number of months, nD is the number of days,
 * nH is the number of hours, nM is the number of minutes, and nS is the number
 * of seconds.
 *
 * <p>
 * Java class for duration complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 *
 * <pre>
 * &lt;complexType name="duration"&gt;
 *   &lt;simpleContent&gt;
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema&gt;duration"&gt;
 *       &lt;attGroup ref="{http://release.niem.gov/niem/structures/5.0/}SimpleObjectAttributeGroup"/&gt;
 *       &lt;anyAttribute processContents='lax' namespace='urn:us:gov:ic:ntk urn:us:gov:ic:ism'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/simpleContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "duration", propOrder = {"value" })
public class Duration {

  @XmlValue
  protected javax.xml.datatype.Duration value;

  @XmlAttribute(name = "id",
    namespace = "http://release.niem.gov/niem/structures/5.0/")
  @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
  @XmlID
  @XmlSchemaType(name = "ID")
  protected java.lang.String id;

  @XmlAttribute(name = "ref",
    namespace = "http://release.niem.gov/niem/structures/5.0/")
  @XmlIDREF
  @XmlSchemaType(name = "IDREF")
  protected Object ref;

  @XmlAttribute(name = "uri",
    namespace = "http://release.niem.gov/niem/structures/5.0/")
  @XmlSchemaType(name = "anyURI")
  protected java.lang.String uri;

  @XmlAttribute(name = "sequenceID",
    namespace = "http://release.niem.gov/niem/structures/5.0/")
  @XmlSchemaType(name = "positiveInteger")
  protected BigInteger sequenceID;

  @XmlAnyAttribute
  private final Map<QName, java.lang.String> otherAttributes =
    new HashMap<QName, java.lang.String>();

  /**
   * Gets the value of the value property.
   *
   * @return
   * possible object is
   * {@link javax.xml.datatype.Duration }
   *
   */
  public javax.xml.datatype.Duration getValue() {

    return value;
  }

  /**
   * Sets the value of the value property.
   *
   * @param value
   * allowed object is
   * {@link javax.xml.datatype.Duration }
   *
   */
  public void setValue(final javax.xml.datatype.Duration value) {

    this.value = value;
  }

  /**
   * Gets the value of the id property.
   *
   * @return
   * possible object is
   * {@link java.lang.String }
   *
   */
  public java.lang.String getId() {

    return id;
  }

  /**
   * Sets the value of the id property.
   *
   * @param value
   * allowed object is
   * {@link java.lang.String }
   *
   */
  public void setId(final java.lang.String value) {

    this.id = value;
  }

  /**
   * Gets the value of the ref property.
   *
   * @return
   * possible object is
   * {@link Object }
   *
   */
  public Object getRef() {

    return ref;
  }

  /**
   * Sets the value of the ref property.
   *
   * @param value
   * allowed object is
   * {@link Object }
   *
   */
  public void setRef(final Object value) {

    this.ref = value;
  }

  /**
   * Gets the value of the uri property.
   *
   * @return
   * possible object is
   * {@link java.lang.String }
   *
   */
  public java.lang.String getUri() {

    return uri;
  }

  /**
   * Sets the value of the uri property.
   *
   * @param value
   * allowed object is
   * {@link java.lang.String }
   *
   */
  public void setUri(final java.lang.String value) {

    this.uri = value;
  }

  /**
   * Gets the value of the sequenceID property.
   *
   * @return
   * possible object is
   * {@link BigInteger }
   *
   */
  public BigInteger getSequenceID() {

    return sequenceID;
  }

  /**
   * Sets the value of the sequenceID property.
   *
   * @param value
   * allowed object is
   * {@link BigInteger }
   *
   */
  public void setSequenceID(final BigInteger value) {

    this.sequenceID = value;
  }

  /**
   * Gets a map that contains attributes that aren't bound to any typed property
   * on this class.
   *
   * <p>
   * the map is keyed by the name of the attribute and
   * the value is the string value of the attribute.
   *
   * the map returned by this method is live, and you can add new attribute
   * by updating the map directly. Because of this design, there's no setter.
   *
   *
   * @return
   * always non-null
   */
  public Map<QName, java.lang.String> getOtherAttributes() {

    return otherAttributes;
  }

}
