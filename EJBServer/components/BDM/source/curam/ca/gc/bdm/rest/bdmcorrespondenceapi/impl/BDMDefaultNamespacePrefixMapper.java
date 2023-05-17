package curam.ca.gc.bdm.rest.bdmcorrespondenceapi.impl;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

/**
 * Implementation of {@link NamespacePrefixMapper} that maps the schema
 * namespaces more to readable names. Used by the jaxb marshaller. Requires
 * setting the property "com.sun.xml.bind.namespacePrefixMapper" to an instance
 * of this class.
 * <p>
 * Requires dependency on JAXB implementation jars
 * </p>
 */
public class BDMDefaultNamespacePrefixMapper extends NamespacePrefixMapper {

  /**
   * Create mappings.
   */
  public BDMDefaultNamespacePrefixMapper() {

  }

  /*
   * (non-Javadoc)
   * Returning null when not found based on spec.
   *
   * @see
   * com.sun.xml.bind.marshaller.NamespacePrefixMapper#getPreferredPrefix(java.
   * lang.String, java.lang.String, boolean)
   */
  @Override
  public String getPreferredPrefix(final String namespaceUri,
    final String suggestion, final boolean requirePrefix) {

    if ("http://interoperability.gc.ca/socialprogram/1.0/"
      .equalsIgnoreCase(namespaceUri)) {
      return "sp";
    }
    if ("http://release.niem.gov/niem/niem-core/5.0/"
      .equalsIgnoreCase(namespaceUri)) {
      return "nc";
    }

    return "";
  }
}
