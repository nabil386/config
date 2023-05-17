package curam.ca.gc.bdm.facade.dynamicevidence.impl;

import curam.codetable.CASEEVIDENCE;
import curam.codetable.CASEPARTICIPANTROLETYPE;
import curam.codetable.CONCERNROLEALTERNATEID;
import curam.codetable.RECORDSTATUS;
import curam.core.fact.MaintainConcernRoleDetailsFactory;
import curam.core.intf.MaintainConcernRoleDetails;
import curam.core.sl.entity.struct.CaseParticipantRoleKey;
import curam.core.sl.entity.struct.ParticipantRoleIDAndNameDetails;
import curam.core.sl.fact.CaseParticipantRoleFactory;
import curam.core.sl.infrastructure.fact.GeneralUtilityFactory;
import curam.core.sl.infrastructure.intf.GeneralUtility;
import curam.core.sl.infrastructure.struct.ConcatenateNameAndAlternateIDSSNKey;
import curam.core.sl.intf.CaseParticipantRole;
import curam.core.sl.struct.EvidenceCaseKey;
import curam.core.sl.struct.SearchCaseParticipantDetailsKey;
import curam.core.sl.struct.SearchCaseParticipantDetailsList;
import curam.core.struct.ConcernRolesDetails;
import curam.core.struct.ReadConcernRoleDetails;
import curam.core.struct.ReadConcernRoleKey;
import curam.dynamicevidence.definition.impl.CaseParticipantSearchType;
import curam.dynamicevidence.definition.impl.EvidenceTypeVersionDef;
import curam.dynamicevidence.definition.impl.RelatedCaseParticipantAttribute;
import curam.dynamicevidence.facade.struct.EvidenceCPInfo;
import curam.dynamicevidence.sl.impl.GenericSLUtil;
import curam.dynamicevidence.util.impl.DateUtil;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.transaction.TransactionInfo;
import curam.util.type.CodeTable;
import curam.util.type.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BDMDynamicEvidenceMaintenance extends
  curam.ca.gc.bdm.facade.dynamicevidence.base.BDMDynamicEvidenceMaintenance {

  @Override
  public SearchCaseParticipantDetailsList
    listCaseParticipant(final EvidenceCaseKey key, final EvidenceCPInfo info)
      throws AppException, InformationalException {

    SearchCaseParticipantDetailsList searchCaseParticipantDetailsList =
      new SearchCaseParticipantDetailsList();

    final SearchCaseParticipantDetailsKey searchKey =
      new SearchCaseParticipantDetailsKey();

    searchKey.caseID = key.caseIDKey.caseID;

    final String relCpName = info.relCpName;

    if ("Null".toLowerCase().equals(info.date)) {
      info.date = "";
    }
    final Date formattedDate = DateUtil.getISODate(info.date);

    EvidenceTypeVersionDef evTypeVersion = null;

    if (!"".equals(info.date)) {
      evTypeVersion = new GenericSLUtil()
        .getEvidenceTypeVersion(formattedDate, key.evidenceKey.evType);

    }

    if (evTypeVersion == null) {
      return searchCaseParticipantDetailsList;

    } else {
      List<CaseParticipantSearchType> searchTypeList = new ArrayList();

      final List<RelatedCaseParticipantAttribute> relCPs =
        evTypeVersion.listRelatedCaseParticipantAttributes();

      Iterator var10 = relCPs.iterator();
      while (var10.hasNext()) {
        final RelatedCaseParticipantAttribute relCP =
          (RelatedCaseParticipantAttribute) var10.next();
        if (relCP.getName().equals(info.relCpName)) {
          searchTypeList = relCP.getEvidenceCPSearchTypeDefs();
          break;
        }
      }

      searchKey.caseParticipantTypeList = new String();
      CaseParticipantSearchType searchType;
      for (var10 = ((List) searchTypeList).iterator(); var10
        .hasNext(); searchKey.caseParticipantTypeList =
          searchKey.caseParticipantTypeList + "|"
            + searchType.getTypeCode().getCode()) {
        searchType = (CaseParticipantSearchType) var10.next();
      }

      int listSize;
      if (searchKey.caseParticipantTypeList == null
        || "".equals(searchKey.caseParticipantTypeList)) {

        final String[] pTypes =
          CodeTable.getAllCodes(CASEPARTICIPANTROLETYPE.TABLENAME,
            TransactionInfo.getProgramLocale());
        final String[] var24 = pTypes;
        listSize = pTypes.length;

        for (int var13 = 0; var13 < listSize; ++var13) {
          final String pType = var24[var13];
          if (pType.length() > 3) {
            final String shortPType = pType.substring(0, 3);

            searchKey.caseParticipantTypeList =
              searchKey.caseParticipantTypeList + "|" + shortPType;

          } else {
            searchKey.caseParticipantTypeList =
              searchKey.caseParticipantTypeList + "|" + pType;

          }
        }
      }

      searchCaseParticipantDetailsList = GeneralUtilityFactory.newInstance()
        .listActiveCaseParticipantsByCaseIDType(searchKey);

      final ConcatenateNameAndAlternateIDSSNKey concatenateNameAndAlternateIDSSNKey =
        new ConcatenateNameAndAlternateIDSSNKey();

      final GeneralUtility generalUtilityObj =
        GeneralUtilityFactory.newInstance();

      concatenateNameAndAlternateIDSSNKey.type =
        CONCERNROLEALTERNATEID.getDefaultCode();
      concatenateNameAndAlternateIDSSNKey.status = RECORDSTATUS.NORMAL;
      concatenateNameAndAlternateIDSSNKey.caseID = key.caseIDKey.caseID;

      listSize =
        searchCaseParticipantDetailsList.searchCaseParticipantDetails.size();

      final MaintainConcernRoleDetails maintainConcernRoleDetailsObj =
        MaintainConcernRoleDetailsFactory.newInstance();

      final CaseParticipantRole caseParticipantRoleObj =
        CaseParticipantRoleFactory.newInstance();

      for (int i = 0; i < listSize; ++i) {

        final CaseParticipantRoleKey caseParticipantRoleKey =
          new CaseParticipantRoleKey();
        caseParticipantRoleKey.caseParticipantRoleID =
          searchCaseParticipantDetailsList.searchCaseParticipantDetails
            .item(i).caseParticipantRoleID;

        final ParticipantRoleIDAndNameDetails participantRoleIDAndName =
          caseParticipantRoleObj
            .readParticipantRoleIDAndParticpantName(caseParticipantRoleKey);

        final ReadConcernRoleKey readConcernRoleKey =
          new ReadConcernRoleKey();
        readConcernRoleKey.concernRoleID =
          participantRoleIDAndName.participantRoleID;

        final ReadConcernRoleDetails readConcernRole =
          maintainConcernRoleDetailsObj.readConcernRole(readConcernRoleKey);

        final ConcernRolesDetails concernRolesDetails =
          new ConcernRolesDetails();
        concernRolesDetails.concernRoleID = readConcernRole.concernRoleID;
        concernRolesDetails.concernRoleType = readConcernRole.concernRoleType;
        concernRolesDetails.concernRoleName = readConcernRole.concernRoleName;

        searchCaseParticipantDetailsList.searchCaseParticipantDetails
          .item(i).name =
            maintainConcernRoleDetailsObj
              .appendAgeToName(concernRolesDetails).concernRoleName;

      }
      if (key.evidenceKey.evType
        .equalsIgnoreCase(CASEEVIDENCE.BIRTH_AND_DEATH_DETAILS)) {
        SearchCaseParticipantDetailsList searchCaseParticipantDetailsList1 =
          new SearchCaseParticipantDetailsList();
        final SearchCaseParticipantDetailsKey searchKey2 =
          new SearchCaseParticipantDetailsKey();

        searchKey2.caseParticipantTypeList = CASEPARTICIPANTROLETYPE.PRIMARY;
        searchKey2.caseID = key.caseIDKey.caseID;

        searchCaseParticipantDetailsList1 = GeneralUtilityFactory
          .newInstance().listActiveCaseParticipantsByCaseIDType(searchKey2);

        return searchCaseParticipantDetailsList1;

      }

      return searchCaseParticipantDetailsList;
    }
  }

}
