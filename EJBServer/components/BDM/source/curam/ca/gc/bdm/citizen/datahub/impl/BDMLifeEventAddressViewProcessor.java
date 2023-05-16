package curam.ca.gc.bdm.citizen.datahub.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.citizen.datahub.impl.Citizen;
import curam.citizen.datahub.impl.CustomViewProcessor;
import curam.citizenworkspace.message.impl.CITIZENDATAHUBExceptionCreator;
import curam.codetable.impl.CONCERNROLEADDRESSTYPEEntry;
import curam.codetable.impl.CONCERNROLETYPEEntry;
import curam.codetable.impl.COUNTRYEntry;
import curam.common.util.xml.dom.DOMException;
import curam.common.util.xml.dom.Document;
import curam.common.util.xml.dom.Element;
import curam.common.util.xml.dom.xpath.XPath;
import curam.core.struct.OtherAddressData;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.participant.impl.ConcernRole;
import curam.participant.impl.ConcernRoleDAO;
import curam.participant.person.impl.Person;
import curam.participant.person.impl.PersonDAO;
import curam.pdc.impl.PDCConst;
import curam.piwrapper.caseheader.impl.CaseHeaderDAO;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BDMLifeEventAddressViewProcessor implements CustomViewProcessor {

  @Inject
  PersonDAO personDAO;

  @Inject
  ConcernRoleDAO concernRoleDAO;

  @Inject
  CaseHeaderDAO caseHeaderDAO;

  @Inject
  private BDMUtil bdmUtil;

  @Inject
  private BDMEvidenceUtil bdmEvidenceUtil;

  protected BDMLifeEventAddressViewProcessor() {

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
  private Element setPersonEntityAttributes(Element personEl,
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
    // set up combo box searchAddress entity
    Element searchAddressEl = null;
    searchAddressEl =
      new Element(BDMLifeEventDatastoreConstants.SEARCH_ADDRESS);
    personEl.addContent(searchAddressEl);

    // Set Residential Address into DataStore
    try {
      personEl = setAddressIntoDatastore(concernRoleID, personEl,
        CONCERNROLEADDRESSTYPEEntry.PRIVATE);
    } catch (final Exception e) {
      e.printStackTrace();
    }
    // Set Mailing Address into DataStore
    try {
      personEl = setAddressIntoDatastore(concernRoleID, personEl,
        CONCERNROLEADDRESSTYPEEntry.MAILING);
    } catch (final Exception e) {
      e.printStackTrace();
    }
    return personEl;
  }

  /**
   *
   * @param concernRoleID
   * @param personEl
   * @param addressType
   * @throws AppException
   * @throws InformationalException
   */
  private Element setAddressIntoDatastore(final long concernRoleID,
    final Element personEl, final CONCERNROLEADDRESSTYPEEntry addressType)
    throws AppException, InformationalException {

    // Get Addresses to prepopulate with
    final BDMEvidenceUtil bdmEvidenceUtil = new BDMEvidenceUtil();
    final List<DynamicEvidenceDataDetails> evidenceDataDetailsList =
      bdmEvidenceUtil.getEvdDtlsByConcernroleIDandEvidenceType(concernRoleID,
        PDCConst.PDCADDRESS);
    // filter out evidence which are enddated
    final List<DynamicEvidenceDataDetails> existingAddressList =
      evidenceDataDetailsList.stream().filter(addressDtls -> addressDtls
        .getAttribute("toDate").getValue().isEmpty())
        .collect(Collectors.toList());
    final List<DynamicEvidenceDataDetails> existingAddressEvdDtls;
    // Get the Address
    existingAddressEvdDtls = existingAddressList.size() > 0
      ? existingAddressList.stream()
        .filter(addressEvdDtls -> addressEvdDtls.getAttribute("addressType")
          .getValue().equals(addressType.getCode()))
        .collect(Collectors.toList())
      : new ArrayList<DynamicEvidenceDataDetails>();
    // Set the Address into the datastore
    if (existingAddressEvdDtls.size() > 0) {
      final OtherAddressData otherAddressData = bdmEvidenceUtil
        .getExistingOtherAddressData(existingAddressEvdDtls.get(0).getID());
      final String addressCountry = bdmUtil.getAddressDetails(
        otherAddressData, BDMConstants.kADDRESSELEMENT_COUNTRY);
      if (addressCountry.equals(COUNTRYEntry.CA.getCode().toString())) {
        Element addressEl = null;
        if (addressType.getCode()
          .equals(CONCERNROLEADDRESSTYPEEntry.PRIVATE.getCode())) {
          addressEl =
            new Element(BDMLifeEventDatastoreConstants.RESIDENTIAL_ADDRESS);
          personEl.setAttribute(
            BDMLifeEventDatastoreConstants.RESIDENTIAL_COUNTRY,
            addressCountry);
          final Element searchAddressEl =
            personEl.getChild(BDMLifeEventDatastoreConstants.SEARCH_ADDRESS);
          searchAddressEl.setAttribute(
            BDMLifeEventDatastoreConstants.SEARCH_ADDRESS_RES_ID, "-2");
          searchAddressEl.setAttribute(
            BDMLifeEventDatastoreConstants.SEARCH_ADDRESS_QUERY_RESIDENTIAL_ADDRESS,
            bdmUtil.getAddressDetails(otherAddressData,
              BDMConstants.kADDRESSELEMENT_POSTALCODE));
        } else if (addressType.getCode()
          .equals(CONCERNROLEADDRESSTYPEEntry.MAILING.getCode())) {
          addressEl =
            new Element(BDMLifeEventDatastoreConstants.MAILING_ADDRESS);
          personEl.setAttribute(
            BDMLifeEventDatastoreConstants.MAILING_COUNTRY, addressCountry);
          final Element searchAddressEl =
            personEl.getChild(BDMLifeEventDatastoreConstants.SEARCH_ADDRESS);
          searchAddressEl.setAttribute(
            BDMLifeEventDatastoreConstants.SEARCH_ADDRESS_MAIL_ID, "-2");
          searchAddressEl.setAttribute(
            BDMLifeEventDatastoreConstants.SEARCH_ADDRESS_QUERY_MAILING_ADDRESS,
            bdmUtil.getAddressDetails(otherAddressData,
              BDMConstants.kADDRESSELEMENT_POSTALCODE));
        } else {
          return personEl;
        }
        addressEl.setAttribute(BDMLifeEventDatastoreConstants.SUITE_NUM,
          bdmUtil.getAddressDetails(otherAddressData,
            BDMConstants.kADDRESSELEMENT_APTUNITNUM));
        addressEl.setAttribute(BDMLifeEventDatastoreConstants.ADDRESS_LINE1,
          bdmUtil.getAddressDetails(otherAddressData,
            BDMConstants.kADDRESSELEMENT_STREETNUM));
        addressEl.setAttribute(BDMLifeEventDatastoreConstants.ADDRESS_LINE2,
          bdmUtil.getAddressDetails(otherAddressData,
            BDMConstants.kADDRESSELEMENT_STREETNAME));
        addressEl.setAttribute(BDMLifeEventDatastoreConstants.CITY,
          bdmUtil.getAddressDetails(otherAddressData,
            BDMConstants.kADDRESSELEMENT_CITY));
        addressEl.setAttribute(BDMLifeEventDatastoreConstants.PROVINCE,
          bdmUtil.getAddressDetails(otherAddressData,
            BDMConstants.kADDRESSELEMENT_PROVINCE));
        addressEl.setAttribute(BDMLifeEventDatastoreConstants.POSTAL_CODE,
          bdmUtil.getAddressDetails(otherAddressData,
            BDMConstants.kADDRESSELEMENT_POSTALCODE));
        addressEl.setAttribute(BDMLifeEventDatastoreConstants.ADDRESS_COUNTRY,
          addressCountry);
        personEl.addContent(addressEl);
      } else {
        // set up IntlResAddress entity
        Element intlAddressEl = null;
        if (addressType.getCode()
          .equals(CONCERNROLEADDRESSTYPEEntry.PRIVATE.getCode())) {
          intlAddressEl = new Element(
            BDMLifeEventDatastoreConstants.INTL_RESIDENTIAL_ADDRESS);
          personEl.setAttribute(
            BDMLifeEventDatastoreConstants.RESIDENTIAL_COUNTRY,
            addressCountry);
          final Element searchAddressEl =
            personEl.getChild(BDMLifeEventDatastoreConstants.SEARCH_ADDRESS);
          searchAddressEl.setAttribute(
            BDMLifeEventDatastoreConstants.SEARCH_ADDRESS_RES_ID,
            BDMLifeEventDatastoreConstants.SEARCH_ADDRESS_ID);
        } else if (addressType.getCode()
          .equals(CONCERNROLEADDRESSTYPEEntry.MAILING.getCode())) {
          intlAddressEl =
            new Element(BDMLifeEventDatastoreConstants.INTL_MAILING_ADDRESS);
          personEl.setAttribute(
            BDMLifeEventDatastoreConstants.MAILING_COUNTRY, addressCountry);
          final Element searchAddressEl =
            personEl.getChild(BDMLifeEventDatastoreConstants.SEARCH_ADDRESS);
          searchAddressEl.setAttribute(
            BDMLifeEventDatastoreConstants.SEARCH_ADDRESS_MAIL_ID,
            BDMLifeEventDatastoreConstants.SEARCH_ADDRESS_ID);
        } else {
          return personEl;
        }
        intlAddressEl.setAttribute(BDMLifeEventDatastoreConstants.SUITE_NUM,
          bdmUtil.getAddressDetails(otherAddressData,
            BDMConstants.kADDRESSELEMENT_APTUNITNUM));
        intlAddressEl.setAttribute(
          BDMLifeEventDatastoreConstants.ADDRESS_LINE1,
          bdmUtil.getAddressDetails(otherAddressData,
            BDMConstants.kADDRESSELEMENT_STREETNUM));
        intlAddressEl.setAttribute(
          BDMLifeEventDatastoreConstants.ADDRESS_LINE2,
          bdmUtil.getAddressDetails(otherAddressData,
            BDMConstants.kADDRESSELEMENT_STREETNAME));
        intlAddressEl.setAttribute(
          BDMLifeEventDatastoreConstants.ADDRESS_LINE4,
          bdmUtil.getAddressDetails(otherAddressData,
            BDMConstants.kADDRESSELEMENT_BDMSTPROVX));
        intlAddressEl.setAttribute(BDMLifeEventDatastoreConstants.CITY,
          bdmUtil.getAddressDetails(otherAddressData,
            BDMConstants.kADDRESSELEMENT_CITY));
        intlAddressEl.setAttribute(BDMLifeEventDatastoreConstants.STATE,
          bdmUtil.getAddressDetails(otherAddressData,
            BDMConstants.kADDRESSELEMENT_STATEPROV));

        if (addressCountry.equals(COUNTRYEntry.US.getCode().toString())) {
          intlAddressEl.setAttribute(BDMLifeEventDatastoreConstants.ZIP_CODE,
            bdmUtil.getAddressDetails(otherAddressData,
              BDMConstants.kADDRESSELEMENT_ZIP));
        } else {
          intlAddressEl.setAttribute(BDMLifeEventDatastoreConstants.ZIP_CODE,
            bdmUtil.getAddressDetails(otherAddressData,
              BDMConstants.kADDRESSELEMENT_BDMZIPX));
        }
        intlAddressEl.setAttribute(
          BDMLifeEventDatastoreConstants.ADDRESS_COUNTRY, addressCountry);
        personEl.addContent(intlAddressEl);
      }
    }
    return personEl;
  }
}
