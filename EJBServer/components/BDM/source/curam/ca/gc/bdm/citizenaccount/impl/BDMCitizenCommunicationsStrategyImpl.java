package curam.ca.gc.bdm.citizenaccount.impl;

import curam.ca.gc.bdm.rest.bdmuacommunicationsapi.struct.BDMConcernRoleRowNumKey;
import curam.citizenaccount.entity.fact.CitizenCommSendByPostTrackerFactory;
import curam.citizenaccount.entity.struct.CitizenCommSendByPostTrackerDtls;
import curam.citizenaccount.entity.struct.CitizenCommSendByPostTrackerDtlsList;
import curam.citizenaccount.entity.struct.CommunicationPostStatusCommunicationID;
import curam.citizenaccount.impl.CitizenCommunicationsStrategy;
import curam.citizenworkspace.codetable.COMMUNICATIONPOSTSTATUS;
import curam.codetable.COMMUNICATIONSTATUS;
import curam.codetable.RECORDSTATUS;
import curam.core.fact.ConcernRoleCommunicationFactory;
import curam.core.impl.EnvVars;
import curam.core.intf.ConcernRoleCommunication;
import curam.core.struct.ConcernRoleCommunicationDtls;
import curam.core.struct.ConcernRoleCommunicationDtlsList;
import curam.core.struct.ConcernRoleCommunicationKey;
import curam.core.struct.ConcernRoleKey;
import curam.util.dataaccess.CuramValueList;
import curam.util.dataaccess.DynamicDataAccess;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.type.DateTime;
import java.util.Iterator;

//// START - TASK: 23187 -
public class BDMCitizenCommunicationsStrategyImpl
  extends CitizenCommunicationsStrategy {

  /**
   *
   * Override listCommunication method to include communications with status
   * Sent,Returned,Void and Resubmitted for UA correspondence list:
   *
   * @param concernRoleKey
   * @return ConcernRoleCommunicationDtlsList
   **/
  @Override
  public ConcernRoleCommunicationDtlsList
    listCitizenCommunications(final ConcernRoleKey concernRoleKey)
      throws AppException, InformationalException {

    new ConcernRoleCommunicationDtlsList();
    final String maxNumberCommunications =
      Configuration.getProperty(EnvVars.ENV_CITIZEN_MAX_COMMUNICATIONS);

    if ("0".equals(maxNumberCommunications)) {
      return new ConcernRoleCommunicationDtlsList();

    } else {
      final int maxCommunications = Integer.parseInt(maxNumberCommunications);
      final BDMConcernRoleRowNumKey concernRoleRowNumKey =
        new BDMConcernRoleRowNumKey();
      final ConcernRoleCommunication concernRoleCommunicationObj =
        ConcernRoleCommunicationFactory.newInstance();

      ConcernRoleCommunicationDtlsList concernRoleCommunicationDtlsList;
      if (Configuration.getDBType().equalsIgnoreCase("DB2")) {

        concernRoleRowNumKey.concernRoleID = concernRoleKey.concernRoleID;
        concernRoleRowNumKey.rowNum = maxCommunications;
        concernRoleCommunicationDtlsList = this.searchCitizenCommunicationDB2(
          concernRoleRowNumKey.concernRoleID, maxCommunications);

      } else if (Configuration.getDBType().equalsIgnoreCase("ORA")) {

        concernRoleRowNumKey.concernRoleID = concernRoleKey.concernRoleID;
        concernRoleRowNumKey.rowNum = maxCommunications;
        concernRoleCommunicationDtlsList = this.searchCitizenCommunicationORA(
          concernRoleRowNumKey.concernRoleID, maxCommunications);

      } else {
        concernRoleCommunicationDtlsList = concernRoleCommunicationObj
          .searchAllClientCommunicationsByConcernRoleID(concernRoleKey);

      }

      return concernRoleCommunicationDtlsList;
    }
  }

  /** OOTB implementation */
  @Override
  public void markCommunicationToSendByPost(
    final ConcernRoleCommunicationKey concernRoleCommunicationKey)
    throws AppException, InformationalException {

    final CitizenCommSendByPostTrackerDtls dtls =
      new CitizenCommSendByPostTrackerDtls();

    dtls.communicationID = concernRoleCommunicationKey.communicationID;
    dtls.requestedDateTime = DateTime.getCurrentDateTime();
    dtls.communicationPostStatus = COMMUNICATIONPOSTSTATUS.DEFAULTCODE;
    CitizenCommSendByPostTrackerFactory.newInstance().insert(dtls);

  }

  /** OOTB implementation */
  @Override
  public CitizenCommSendByPostTrackerDtlsList
    searchCitizenCommSendByPostTrackerByConcernRoleID(
      final CommunicationPostStatusCommunicationID communicationPostStatusCommunicationID)
      throws AppException, InformationalException {

    final CitizenCommSendByPostTrackerDtlsList list =
      CitizenCommSendByPostTrackerFactory.newInstance()
        .searchdByConcernRoleIDUsingCommunicationID(
          communicationPostStatusCommunicationID);

    return list;
  }

  protected ConcernRoleCommunicationDtlsList searchCitizenCommunicationDB2(
    final long concernRoleID, final int maxCommunications)
    throws AppException, InformationalException {

    // Custom Struct to hold communication status
    final BDMConcernRoleRowNumKey concernRoleRowNumKey =
      new BDMConcernRoleRowNumKey();

    concernRoleRowNumKey.concernRoleID = concernRoleID;
    concernRoleRowNumKey.rowNum = maxCommunications;
    concernRoleRowNumKey.communicationStatus1 = COMMUNICATIONSTATUS.RETURNED;
    concernRoleRowNumKey.communicationStatus2 = COMMUNICATIONSTATUS.SENT;
    concernRoleRowNumKey.communicationStatus3 =
      COMMUNICATIONSTATUS.RESUBMITTED;
    concernRoleRowNumKey.communicationStatus4 = COMMUNICATIONSTATUS.VOID;

    final StringBuffer sBuf = new StringBuffer();
    final ConcernRoleCommunicationDtlsList concernRoleCommunicationDtlsList =
      new ConcernRoleCommunicationDtlsList();

    sBuf.append(
      "SELECT ConcernRoleCommunication.communicationID, ConcernRoleCommunication.concernRoleID, ConcernRoleCommunication.typeCode, ConcernRoleCommunication.proFormaVersionNo, ConcernRoleCommunication.methodTypeCode,");

    sBuf.append(
      "ConcernRoleCommunication.incomingInd, ConcernRoleCommunication.communicationDate, ConcernRoleCommunication.statusCode, ConcernRoleCommunication.correspondentName, ConcernRoleCommunication.correspondentConcernRoleID, ConcernRoleCommunication.correspondentTypeCode, ");

    sBuf.append(
      "ConcernRoleCommunication.subjectText, ConcernRoleCommunication.documentLocation, ConcernRoleCommunication.documentRefNumber, ConcernRoleCommunication.fileLocation, ConcernRoleCommunication.fileReferenceNumber, ConcernRoleCommunication.caseID, ConcernRoleCommunication.ticketID, ConcernRoleCommunication.userName, ");

    sBuf.append(
      "ConcernRoleCommunication.comments, ConcernRoleCommunication.communicationText, ConcernRoleCommunication.addressID, ConcernRoleCommunication.phoneNumberID, ConcernRoleCommunication.emailAddressID, ConcernRoleCommunication.proFormaID, ConcernRoleCommunication.communicationStatus, ConcernRoleCommunication.proFormaInd,");

    sBuf.append(
      "ConcernRoleCommunication.attachmentInd, ConcernRoleCommunication.documentTemplateID, ConcernRoleCommunication.communicationFormat, ConcernRoleCommunication.localeIdentifier,  ConcernRoleCommunication.versionNo ");

    sBuf.append(" INTO :communicationID, :concernRoleID,");
    sBuf.append(
      ":typeCode,:proFormaVersionNo,:methodTypeCode,:incomingInd,:communicationDate,:statusCode,:correspondentName,:correspondentConcernRoleID,");

    sBuf.append(
      ":correspondentTypeCode,:subjectText,:documentLocation,:documentRefNumber,:fileLocation,:fileReferenceNumber,:caseID,:ticketID,");

    sBuf.append(
      ":userName,:comments,:communicationText,:addressID,:phoneNumberID,:emailAddressID,:proFormaID,:communicationStatus,:proFormaInd,");

    sBuf.append(
      ":attachmentInd,:documentTemplateID,:communicationFormat,:localeIdentifier,:versionNo FROM ConcernRoleCommunication, ConcernRole");

    sBuf.append(
      " WHERE    ( ConcernRoleCommunication.concernRoleID =   :concernRoleID ");

    sBuf.append(
      " OR  ConcernRoleCommunication.correspondentConcernRoleID = :concernRoleID ) AND   ConcernRole.concernRoleID = ConcernRoleCommunication.correspondentConcernRoleID AND  ");

    sBuf.append(
      " ConcernRoleCommunication.statusCode = :statusCode AND (ConcernRoleCommunication.communicationStatus = :communicationStatus1 OR ConcernRoleCommunication.communicationStatus = :communicationStatus2 OR ConcernRoleCommunication.communicationStatus = :communicationStatus3 OR ConcernRoleCommunication.communicationStatus = :communicationStatus4)");

    sBuf.append(
      " ORDER BY ConcernRoleCommunication.communicationDate DESC FETCH FIRST "
        + maxCommunications + " ROWS ONLY ");

    final CuramValueList<ConcernRoleCommunicationDtls> curamValueList =
      DynamicDataAccess.executeNsMulti(ConcernRoleCommunicationDtls.class,
        concernRoleRowNumKey, false, sBuf.toString());

    final Iterator var8 = curamValueList.iterator();
    while (var8.hasNext()) {
      final ConcernRoleCommunicationDtls concernRoleCommunicationDtls =
        (ConcernRoleCommunicationDtls) var8.next();
      concernRoleCommunicationDtlsList.dtls
        .addRef(concernRoleCommunicationDtls);

    }

    return concernRoleCommunicationDtlsList;
  }

  protected ConcernRoleCommunicationDtlsList searchCitizenCommunicationORA(
    final long concernRoleID, final int maxCommunications)
    throws AppException, InformationalException {

    // Custom Struct to hold communication status
    final BDMConcernRoleRowNumKey concernRoleRowNumKey =
      new BDMConcernRoleRowNumKey();

    concernRoleRowNumKey.concernRoleID = concernRoleID;
    concernRoleRowNumKey.rowNum = maxCommunications;
    concernRoleRowNumKey.communicationStatus1 = COMMUNICATIONSTATUS.RETURNED;
    concernRoleRowNumKey.communicationStatus2 = COMMUNICATIONSTATUS.SENT;
    concernRoleRowNumKey.communicationStatus3 =
      COMMUNICATIONSTATUS.RESUBMITTED;
    concernRoleRowNumKey.communicationStatus4 = COMMUNICATIONSTATUS.VOID;

    concernRoleRowNumKey.statusCode = RECORDSTATUS.NORMAL;

    final StringBuffer sBuf = new StringBuffer();
    final ConcernRoleCommunicationDtlsList concernRoleCommunicationDtlsList =
      new ConcernRoleCommunicationDtlsList();

    sBuf.append(
      "SELECT ConcernRoleCommunication.communicationID, ConcernRoleCommunication.concernRoleID, ConcernRoleCommunication.typeCode, ConcernRoleCommunication.proFormaVersionNo, ConcernRoleCommunication.methodTypeCode,");

    sBuf.append(
      "ConcernRoleCommunication.incomingInd, ConcernRoleCommunication.communicationDate, ConcernRoleCommunication.statusCode, ConcernRoleCommunication.correspondentName, ConcernRoleCommunication.correspondentConcernRoleID, ConcernRoleCommunication.correspondentTypeCode, ");

    sBuf.append(
      "ConcernRoleCommunication.subjectText, ConcernRoleCommunication.documentLocation, ConcernRoleCommunication.documentRefNumber, ConcernRoleCommunication.fileLocation, ConcernRoleCommunication.fileReferenceNumber, ConcernRoleCommunication.caseID, ConcernRoleCommunication.ticketID, ConcernRoleCommunication.userName, ");

    sBuf.append(
      "ConcernRoleCommunication.comments, ConcernRoleCommunication.communicationText, ConcernRoleCommunication.addressID, ConcernRoleCommunication.phoneNumberID, ConcernRoleCommunication.emailAddressID, ConcernRoleCommunication.proFormaID, ConcernRoleCommunication.communicationStatus, ConcernRoleCommunication.proFormaInd,");

    sBuf.append(
      "ConcernRoleCommunication.attachmentInd, ConcernRoleCommunication.documentTemplateID, ConcernRoleCommunication.communicationFormat, ConcernRoleCommunication.localeIdentifier,  ConcernRoleCommunication.versionNo ");

    sBuf.append(" INTO :communicationID, :concernRoleID,");
    sBuf.append(
      ":typeCode,:proFormaVersionNo,:methodTypeCode,:incomingInd,:communicationDate,:statusCode,:correspondentName,:correspondentConcernRoleID,");

    sBuf.append(
      ":correspondentTypeCode,:subjectText,:documentLocation,:documentRefNumber,:fileLocation,:fileReferenceNumber,:caseID,:ticketID,");

    sBuf.append(
      ":userName,:comments,:communicationText,:addressID,:phoneNumberID,:emailAddressID,:proFormaID,:communicationStatus,:proFormaInd,");

    sBuf.append(
      ":attachmentInd,:documentTemplateID,:communicationFormat,:localeIdentifier,:versionNo FROM ConcernRoleCommunication, ConcernRole");

    sBuf.append(
      " WHERE    ( ConcernRoleCommunication.concernRoleID =   :concernRoleID ");

    sBuf.append(
      " OR  ConcernRoleCommunication.correspondentConcernRoleID = :concernRoleID ) AND   ConcernRole.concernRoleID = ConcernRoleCommunication.correspondentConcernRoleID AND  ");

    sBuf.append(
      " ConcernRoleCommunication.statusCode = :statusCode AND (ConcernRoleCommunication.communicationStatus = :communicationStatus1 OR ConcernRoleCommunication.communicationStatus = :communicationStatus2 OR ConcernRoleCommunication.communicationStatus = :communicationStatus3 OR ConcernRoleCommunication.communicationStatus = :communicationStatus4)");

    sBuf.append(
      " AND rownum <= :rowNum ORDER BY ConcernRoleCommunication.communicationDate DESC ");

    final CuramValueList<ConcernRoleCommunicationDtls> curamValueList =
      DynamicDataAccess.executeNsMulti(ConcernRoleCommunicationDtls.class,
        concernRoleRowNumKey, false, sBuf.toString());

    final Iterator var8 = curamValueList.iterator();
    while (var8.hasNext()) {
      final ConcernRoleCommunicationDtls concernRoleCommunicationDtls =
        (ConcernRoleCommunicationDtls) var8.next();
      concernRoleCommunicationDtlsList.dtls
        .addRef(concernRoleCommunicationDtls);
    }
    return concernRoleCommunicationDtlsList;
  }
  //// END - TASK: 23187 -
}
