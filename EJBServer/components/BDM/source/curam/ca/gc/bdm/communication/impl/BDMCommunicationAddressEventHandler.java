package curam.ca.gc.bdm.communication.impl;

import curam.ca.gc.bdm.codetable.BDMRECIPIENTCONTACTTYPE;
import curam.ca.gc.bdm.entity.communication.fact.BDMConcernRoleCommunicationFactory;
import curam.ca.gc.bdm.entity.communication.intf.BDMConcernRoleCommunication;
import curam.ca.gc.bdm.entity.communication.struct.BDMConcernRoleCommunicationDtls;
import curam.ca.gc.bdm.entity.communication.struct.BDMConcernRoleCommunicationKey;
import curam.ca.gc.bdm.entity.communication.struct.BDMConcernRoleCommunicationKeyList;
import curam.ca.gc.bdm.facade.communication.struct.BDMCorrespondenceDetails;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.rest.bdmcorrespondenceapi.impl.BDMGenerateCorrespondenceMapper;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.BDMCCTOutboundInterfaceImpl;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos.BDMCCTUpdateCorrespondenceRequest;
import curam.codetable.CONCERNROLEADDRESSTYPE;
import curam.core.fact.ConcernRoleAddressFactory;
import curam.core.fact.ConcernRoleCommunicationFactory;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.core.intf.ConcernRoleCommunication;
import curam.core.struct.ConcernRoleAddressDtls;
import curam.core.struct.ConcernRoleAddressKey;
import curam.core.struct.ConcernRoleCommunicationDtls;
import curam.core.struct.ConcernRoleCommunicationKey;
import curam.core.struct.ConcernRoleKey;
import curam.util.events.impl.EventFilter;
import curam.util.events.impl.EventHandler;
import curam.util.events.struct.Event;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.resources.StringUtil;
import curam.util.resources.Trace;
import curam.util.transaction.TransactionInfo;
import curam.util.type.CodeTable;
import curam.util.type.NotFoundIndicator;
import java.io.IOException;
import javax.xml.bind.JAXBException;

public class BDMCommunicationAddressEventHandler
  implements EventHandler, EventFilter {

  @Override
  public boolean accept(final Event event)
    throws AppException, InformationalException {

    // Accept all events for this event class
    return true;
  }

  @Override
  public void eventRaised(final Event event)
    throws AppException, InformationalException {

    final ConcernRoleAddressKey addressKey = new ConcernRoleAddressKey();
    addressKey.concernRoleAddressID = event.primaryEventData;

    // Get the concernRoleID associated with the address

    final NotFoundIndicator notFoundIndicator = new NotFoundIndicator();
    final ConcernRoleAddressDtls concernRoleAddressDtls =
      ConcernRoleAddressFactory.newInstance().read(notFoundIndicator,
        addressKey);
    // Further processing is needed only if mailing address is modified
    if (!notFoundIndicator.isNotFound()) {
      if (!CONCERNROLEADDRESSTYPE.MAILING
        .equals(concernRoleAddressDtls.typeCode)) {
        return;
      }
      boolean toModified = false;
      boolean ccModified = false;
      // read thru communication records to check if a communication exists for
      // 1. This concernRoleID
      // 2. Status of DRAFT or SUBMITTED
      // 3. Format is correspondence
      // 4. Record Status is active

      final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
      concernRoleKey.concernRoleID = concernRoleAddressDtls.concernRoleID;
      final BDMConcernRoleCommunication bdmCommObj =
        BDMConcernRoleCommunicationFactory.newInstance();
      final BDMConcernRoleCommunicationKeyList communicationList =
        bdmCommObj.searchPendingRecordsForAddressUpdate(concernRoleKey);
      final ConcernRoleCommunication communicationObj =
        ConcernRoleCommunicationFactory.newInstance();
      final ConcernRoleCommunicationKey concernRoleCommunicationKey =
        new ConcernRoleCommunicationKey();
      ConcernRoleCommunicationDtls concernRoleCommunicationDtls = null;
      final BDMCommunicationHelper helperObj = new BDMCommunicationHelper();
      BDMCorrespondenceDetails correspondenceDetails;
      for (final BDMConcernRoleCommunicationKey communicationKey : communicationList.dtls) {
        correspondenceDetails = new BDMCorrespondenceDetails();
        // Read the communication
        concernRoleCommunicationKey.communicationID =
          communicationKey.communicationID;
        concernRoleCommunicationDtls = communicationObj
          .read(notFoundIndicator, concernRoleCommunicationKey);
        if (!notFoundIndicator.isNotFound()
          && null != concernRoleCommunicationDtls) {

          // Read BDMConcernRoleCommunication details
          final BDMConcernRoleCommunicationDtls bdmCommDtls =
            bdmCommObj.read(notFoundIndicator, communicationKey);
          if (!notFoundIndicator.isNotFound()) {
            // If the type is not captured, skip the record.
            if (StringUtil.isNullOrEmpty(bdmCommDtls.toRecipientContactType)
              && StringUtil
                .isNullOrEmpty(bdmCommDtls.ccRecipientContactType)) {
              continue;
            }

            // Read correspondent concern roleID
            final long toConcernRoleID =
              concernRoleCommunicationDtls.correspondentConcernRoleID;
            // If the concernRoleID is the same as the participant whose
            // address is changed, then calculate the latest address.
            final long toAddressID =
              helperObj.getAddressIDforConcern(toConcernRoleID,
                !BDMRECIPIENTCONTACTTYPE.CLIENT
                  .equalsIgnoreCase(bdmCommDtls.toRecipientContactType)
                  && !BDMRECIPIENTCONTACTTYPE.CONTACT
                    .equalsIgnoreCase(bdmCommDtls.toRecipientContactType));
            if (CuramConst.gkZero != toAddressID) {
              final String[] toAddress =
                helperObj.getAddressMapper(toAddressID);
              correspondenceDetails.toDetails.addressLineOne = toAddress[0];
              correspondenceDetails.toDetails.addressLineTwo = toAddress[1];
              correspondenceDetails.toDetails.addressLineThree = toAddress[2];
              correspondenceDetails.toDetails.addressLineFour = toAddress[3];
              if (!StringUtil.isNullOrEmpty(toAddress[4])
                && BDMConstants.kBDMUNPARSE.equalsIgnoreCase(toAddress[4])) {
                correspondenceDetails.toDetails.unparsedAddressInd = true;
              }
              correspondenceDetails.toRecipientContactCode =
                CodeTable.getOneItem(BDMRECIPIENTCONTACTTYPE.TABLENAME,
                  bdmCommDtls.toRecipientContactType);
              correspondenceDetails.toCorrespondentName =
                helperObj.getConcernRoleName(toConcernRoleID);
              // populate language
              correspondenceDetails.toParticipantPreferredLanguage =
                helperObj.getPreferredLanguage(toConcernRoleID);
              // populate tracking number
              correspondenceDetails.trackingNumber =
                bdmCommDtls.trackingNumber;
              if (concernRoleCommunicationDtls.addressID != toAddressID) {
                concernRoleCommunicationDtls.addressID = toAddressID;
                toModified = true;
              }
            }

            // Get the cc correspondent if one exists.
            // The cc can only be a 3rd party
            if (CuramConst.gkZero != bdmCommDtls.ccThirdPartyContactConcernRoleID) {
              // get the cc addressID
              final long ccAddressID = helperObj.getAddressIDforConcern(
                bdmCommDtls.ccThirdPartyContactConcernRoleID, true);
              if (CuramConst.gkZero != ccAddressID) {
                final String[] ccAddress =
                  helperObj.getAddressMapper(ccAddressID);
                correspondenceDetails.ccThirdPartyContactConcernRoleID =
                  bdmCommDtls.ccThirdPartyContactConcernRoleID;
                correspondenceDetails.ccDetails.ccAddressLineOne =
                  ccAddress[0];
                correspondenceDetails.ccDetails.ccAddressLineTwo =
                  ccAddress[1];
                correspondenceDetails.ccDetails.ccAddressLineThree =
                  ccAddress[2];
                correspondenceDetails.ccDetails.ccAddressLineFour =
                  ccAddress[3];
                if (!StringUtil.isNullOrEmpty(ccAddress[4])
                  && BDMConstants.kBDMUNPARSE
                    .equalsIgnoreCase(ccAddress[4])) {
                  correspondenceDetails.ccDetails.ccUnparsedAddressInd = true;
                }
                correspondenceDetails.ccRecipientContactCode =
                  CodeTable.getOneItem(BDMRECIPIENTCONTACTTYPE.TABLENAME,
                    bdmCommDtls.ccRecipientContactType);
                correspondenceDetails.ccCorrespondentName =
                  helperObj.getConcernRoleName(
                    bdmCommDtls.ccThirdPartyContactConcernRoleID);
                // populate language
                correspondenceDetails.ccContactPreferredLanguage =
                  helperObj.getPreferredLanguage(
                    bdmCommDtls.ccThirdPartyContactConcernRoleID);
                if (bdmCommDtls.ccAddressID != ccAddressID) {
                  bdmCommDtls.ccAddressID = ccAddressID;
                  ccModified = true;
                }
              }
            }

            final BDMGenerateCorrespondenceMapper mapper =
              new BDMGenerateCorrespondenceMapper();

            try {
              final String dataXml =
                mapper.addressUpdateMapper(correspondenceDetails);
              final BDMCCTUpdateCorrespondenceRequest updateRequest =
                populateUpdateRequest(bdmCommDtls.workItemID, dataXml);

              final BDMCCTOutboundInterfaceImpl outBoundInterfaceImplObj =
                new BDMCCTOutboundInterfaceImpl();
              if (outBoundInterfaceImplObj
                .updateCorrespondence(updateRequest)) {
                // Persist the new addressIDs only if the update to CCT
                // is successful. Else mismatch of addresses would occur.
                if (toModified) {
                  communicationObj.modify(concernRoleCommunicationKey,
                    concernRoleCommunicationDtls);
                }
                if (ccModified) {
                  bdmCommObj.modify(communicationKey, bdmCommDtls);
                }
              }
            } catch (final JAXBException e) {
              Trace.kTopLevelLogger.error(e);
            } catch (final IOException ie) {
              Trace.kTopLevelLogger.error(ie);
            } catch (final AppException ae) {
              Trace.kTopLevelLogger.error(ae);
            }
          }
        }
      }
    }
  }

  /**
   * This method populates the update request to CCT.
   *
   * @param workItemID
   * @param dataXml
   * @return
   */
  private BDMCCTUpdateCorrespondenceRequest
    populateUpdateRequest(final long workItemID, final String dataXml) {

    final BDMCCTUpdateCorrespondenceRequest updateRequest =
      new BDMCCTUpdateCorrespondenceRequest();
    updateRequest.setCommunity(
      Configuration.getProperty(EnvVars.BDM_CCT_API_COMMUNITY_VALUE));
    updateRequest.setDataMapName(BDMConstants.kCCTDataMapName);
    updateRequest.setDataXML(dataXml);
    updateRequest.setUserId(TransactionInfo.getProgramUser());
    updateRequest.setWorkItemId(workItemID);

    return updateRequest;
  }

}
