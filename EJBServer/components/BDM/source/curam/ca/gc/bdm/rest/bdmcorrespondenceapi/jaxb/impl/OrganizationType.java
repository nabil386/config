//
// This file was generated by the Eclipse Implementation of JAXB, v3.0.1
// See https://eclipse-ee4j.github.io/jaxb-ri
// Any modifications to this file will be lost upon recompilation of the source
// schema.
// Generated on: 2022.10.17 at 01:30:31 PM EDT
//

package curam.ca.gc.bdm.rest.bdmcorrespondenceapi.jaxb.impl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * A data type for a body of people organized for a particular purpose.
 *
 * <p>
 * Java class for OrganizationType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 *
 * <pre>
 * &lt;complexType name="OrganizationType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://release.niem.gov/niem/structures/5.0/}ObjectType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://release.niem.gov/niem/niem-core/5.0/}OrganizationCategoryText" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element ref="{http://release.niem.gov/niem/niem-core/5.0/}OrganizationDescriptionText" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element ref="{http://release.niem.gov/niem/niem-core/5.0/}OrganizationIdentification" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element ref="{http://release.niem.gov/niem/niem-core/5.0/}OrganizationLocation" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element ref="{http://release.niem.gov/niem/niem-core/5.0/}OrganizationName" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;anyAttribute processContents='lax' namespace='urn:us:gov:ic:ntk urn:us:gov:ic:ism'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OrganizationType",
  namespace = "http://release.niem.gov/niem/niem-core/5.0/",
  propOrder = {"organizationCategoryText", "organizationDescriptionText",
    "organizationIdentification", "organizationLocation",
    "organizationName" })
public class OrganizationType extends ObjectType {

  @XmlElement(name = "OrganizationCategoryText", nillable = true)
  protected List<TextType> organizationCategoryText;

  @XmlElement(name = "OrganizationDescriptionText", nillable = true)
  protected List<TextType> organizationDescriptionText;

  @XmlElement(name = "OrganizationIdentification", nillable = true)
  protected List<IdentificationType> organizationIdentification;

  @XmlElement(name = "OrganizationLocation", nillable = true)
  protected List<LocationType> organizationLocation;

  @XmlElement(name = "OrganizationName", nillable = true)
  protected List<TextType> organizationName;

  /**
   * Gets the value of the organizationCategoryText property.
   *
   * <p>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the Jakarta XML Binding object.
   * This is why there is not a <CODE>set</CODE> method for the
   * organizationCategoryText property.
   *
   * <p>
   * For example, to add a new item, do as follows:
   *
   * <pre>
   * getOrganizationCategoryText().add(newItem);
   * </pre>
   *
   *
   * <p>
   * Objects of the following type(s) are allowed in the list
   * {@link TextType }
   *
   *
   */
  public List<TextType> getOrganizationCategoryText() {

    if (organizationCategoryText == null) {
      organizationCategoryText = new ArrayList<TextType>();
    }
    return this.organizationCategoryText;
  }

  /**
   * Gets the value of the organizationDescriptionText property.
   *
   * <p>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the Jakarta XML Binding object.
   * This is why there is not a <CODE>set</CODE> method for the
   * organizationDescriptionText property.
   *
   * <p>
   * For example, to add a new item, do as follows:
   *
   * <pre>
   * getOrganizationDescriptionText().add(newItem);
   * </pre>
   *
   *
   * <p>
   * Objects of the following type(s) are allowed in the list
   * {@link TextType }
   *
   *
   */
  public List<TextType> getOrganizationDescriptionText() {

    if (organizationDescriptionText == null) {
      organizationDescriptionText = new ArrayList<TextType>();
    }
    return this.organizationDescriptionText;
  }

  /**
   * Gets the value of the organizationIdentification property.
   *
   * <p>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the Jakarta XML Binding object.
   * This is why there is not a <CODE>set</CODE> method for the
   * organizationIdentification property.
   *
   * <p>
   * For example, to add a new item, do as follows:
   *
   * <pre>
   * getOrganizationIdentification().add(newItem);
   * </pre>
   *
   *
   * <p>
   * Objects of the following type(s) are allowed in the list
   * {@link IdentificationType }
   *
   *
   */
  public List<IdentificationType> getOrganizationIdentification() {

    if (organizationIdentification == null) {
      organizationIdentification = new ArrayList<IdentificationType>();
    }
    return this.organizationIdentification;
  }

  /**
   * Gets the value of the organizationLocation property.
   *
   * <p>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the Jakarta XML Binding object.
   * This is why there is not a <CODE>set</CODE> method for the
   * organizationLocation property.
   *
   * <p>
   * For example, to add a new item, do as follows:
   *
   * <pre>
   * getOrganizationLocation().add(newItem);
   * </pre>
   *
   *
   * <p>
   * Objects of the following type(s) are allowed in the list
   * {@link LocationType }
   *
   *
   */
  public List<LocationType> getOrganizationLocation() {

    if (organizationLocation == null) {
      organizationLocation = new ArrayList<LocationType>();
    }
    return this.organizationLocation;
  }

  /**
   * Gets the value of the organizationName property.
   *
   * <p>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the Jakarta XML Binding object.
   * This is why there is not a <CODE>set</CODE> method for the organizationName
   * property.
   *
   * <p>
   * For example, to add a new item, do as follows:
   *
   * <pre>
   * getOrganizationName().add(newItem);
   * </pre>
   *
   *
   * <p>
   * Objects of the following type(s) are allowed in the list
   * {@link TextType }
   *
   *
   */
  public List<TextType> getOrganizationName() {

    if (organizationName == null) {
      organizationName = new ArrayList<TextType>();
    }
    return this.organizationName;
  }

}
