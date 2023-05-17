/**
 *
 */
package curam.ca.gc.bdm.util.impl;

import curam.ca.gc.bdm.codetable.BDMATTACHMENTLINKTYPE;
import curam.ca.gc.bdm.entity.fact.BDMFEAttachmentLinkFactory;
import curam.ca.gc.bdm.entity.fact.BDMForeignApplicationFactory;
import curam.ca.gc.bdm.entity.fact.BDMTaskFactory;
import curam.ca.gc.bdm.entity.intf.BDMFEAttachmentLink;
import curam.ca.gc.bdm.entity.struct.BDMFEAttachmentLinkDtls;
import curam.ca.gc.bdm.entity.struct.BDMFEAttachmentLinkDtlsList;
import curam.ca.gc.bdm.entity.struct.BDMFEAttachmentLinkKeyStruct3;
import curam.ca.gc.bdm.entity.struct.BDMForeignApplicationDtls;
import curam.ca.gc.bdm.entity.struct.BDMForeignApplicationKey;
import curam.ca.gc.bdm.entity.struct.BDMTaskDtls;
import curam.ca.gc.bdm.entity.struct.BDMTaskKey;
import curam.ca.gc.bdm.sl.bdminbox.struct.BDMEscalationLevelString;
import curam.ca.gc.bdm.sl.bdminbox.struct.CaseUrgentFlagStringDetails;
import curam.ca.gc.bdm.sl.organization.supervisor.fact.BDMMaintainSupervisorUsersFactory;
import curam.codetable.BDMTASKCATEGORY;
import curam.codetable.BDMTASKTYPE;
import curam.codetable.BUSINESSOBJECTTYPE;
import curam.codetable.RECORDSTATUS;
import curam.core.impl.CuramConst;
import curam.core.sl.entity.struct.TaskKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.StringUtil;
import curam.util.transaction.TransactionInfo;
import curam.util.type.CodeTable;
import curam.util.type.NotFoundIndicator;
import curam.util.workflow.fact.BizObjAssociationFactory;
import curam.util.workflow.intf.BizObjAssociation;
import curam.util.workflow.struct.BizObjAssociationDtls;
import curam.util.workflow.struct.BizObjAssociationDtlsList;

/**
 * @author donghua.jin
 *
 */
public class BDMTaskUtil {

  private BDMTaskUtil() {

    // no instance
  }

  /**
   * Retrieve the urgent flags associated with the task.
   *
   * @param taskID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public static String getCaseUrgentFlagsByTaskID(final long taskID)
    throws AppException, InformationalException {

    final TaskKey tKey = new TaskKey();
    tKey.taskID = taskID;
    final CaseUrgentFlagStringDetails details =
      BDMMaintainSupervisorUsersFactory.newInstance()
        .getCaseUrgentFlagsByTaskID(tKey);

    return details.caseUrgentFlagStr;// TODO fix this
  }

  /**
   * Retrieve the escalation levels associated with the task.
   *
   * @param taskID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public static String getEscalationLevelsByTaskID(final long taskID)
    throws AppException, InformationalException {

    final TaskKey tKey = new TaskKey();
    tKey.taskID = taskID;
    final BDMEscalationLevelString details = BDMMaintainSupervisorUsersFactory
      .newInstance().readEscalationLevelByTaskID(tKey);

    return details.escalationLevelDesc;
  }

  /**
   * Get task category description for the task.
   *
   * @param taskID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public static String getTaskCategoryDesc(final long taskID)
    throws AppException, InformationalException {

    String desc = "";

    final BDMTaskDtls bdmTaskDtls = getBDMTaskDtls(taskID);

    if (bdmTaskDtls != null && !StringUtil.isNullOrEmpty(bdmTaskDtls.type)) {
      final String parentCode =
        CodeTable.getParentCode(BDMTASKTYPE.TABLENAME, bdmTaskDtls.type);

      desc = CodeTable.getOneItem(BDMTASKCATEGORY.TABLENAME, parentCode,
        TransactionInfo.getProgramLocale());
    }

    return desc;
  }

  /**
   * Get task type description for the task.
   *
   * @param taskID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public static String getTaskTypeDesc(final long taskID)
    throws AppException, InformationalException {

    String desc = "";

    final BDMTaskDtls bdmTaskDtls = getBDMTaskDtls(taskID);

    if (bdmTaskDtls != null) {
      desc = CodeTable.getOneItem(BDMTASKTYPE.TABLENAME, bdmTaskDtls.type,
        TransactionInfo.getProgramLocale());
    }

    return desc;
  }

  /**
   * Get BDMTask entity dtls object.
   *
   * @param taskID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  protected static BDMTaskDtls getBDMTaskDtls(final long taskID)
    throws AppException, InformationalException {

    final BDMTaskKey taskKey = new BDMTaskKey();
    taskKey.taskID = taskID;
    final NotFoundIndicator nfi = new NotFoundIndicator();

    return BDMTaskFactory.newInstance().read(nfi, taskKey);
  }

  /**
   * This is a utility method, it will find out the task type and determine its
   * kind against a list of task types.
   *
   * @author hamed.mohammed
   * @param taskID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public static boolean isTaskCreatedForTaskITaskIITaskVI(final long taskID)
    throws AppException, InformationalException {

    boolean taskTypeFoundInd = false;

    final BDMTaskDtls bdmTaskDtls = getBDMTaskDtls(taskID);

    if (bdmTaskDtls != null) {

      if (bdmTaskDtls.type.equals(BDMTASKTYPE.BDMFBAPPLICATIONS)
        || bdmTaskDtls.type.equals(BDMTASKTYPE.BDMFBAPPLICATIONDISABILITY)
        || bdmTaskDtls.type.equals(BDMTASKTYPE.BDMFBBESS)
        || bdmTaskDtls.type.equals(BDMTASKTYPE.BDMFBLIASONMEDICAL)
        || bdmTaskDtls.type.equals(BDMTASKTYPE.BDMFBLIASONNONMEDICAL)
        || bdmTaskDtls.type.equals(BDMTASKTYPE.FB_INTERIM_APP)
        || bdmTaskDtls.type.equals(BDMTASKTYPE.BDMFBADDRESS)
        || bdmTaskDtls.type
          .equals(BDMTASKTYPE.ADDRESSCHANGE_DIRECTDEPOSIT_PWS)
        || bdmTaskDtls.type.equals(BDMTASKTYPE.BDMFBCONSENTTOCUMMUNICATE1603)
        || bdmTaskDtls.type
          .equals(BDMTASKTYPE.BDMFBAUTHORIZATIONTOCOMMUNICATE3015)
        || bdmTaskDtls.type.equals(BDMTASKTYPE.BDMFBCORRESPONDENCE)
        || bdmTaskDtls.type.equals(BDMTASKTYPE.BDMFBPOA)
        || bdmTaskDtls.type.equals(BDMTASKTYPE.BDMFBTRUSTEE)) {

        taskTypeFoundInd = true;

      }

    }

    return taskTypeFoundInd;
  }

  /**
   * This is utility method, which will find out related foreign application
   * linked to an attachment.
   *
   * @author hamed.mohammed
   * @param taskID
   * @throws AppException
   * @throws InformationalException
   */
  public static void findFAandUpdateFAStatus(long taskID, String faStatusCode)
    throws AppException, InformationalException {

    long fApplicationID = CuramConst.kLongZero;

    if (BDMTaskUtil.isTaskCreatedForTaskITaskIITaskVI(taskID)) {

      BizObjAssociation bizObjAssociation =
        BizObjAssociationFactory.newInstance();

      curam.util.workflow.struct.TaskKey taskKey2 =
        new curam.util.workflow.struct.TaskKey();

      taskKey2.taskID = taskID;

      BizObjAssociationDtlsList bizObjAssociationDtlsList =
        bizObjAssociation.searchByTaskID(taskKey2);

      for (BizObjAssociationDtls bizObjAssociationDtls : bizObjAssociationDtlsList.dtls) {

        if (bizObjAssociationDtls.bizObjectType
          .equals(BUSINESSOBJECTTYPE.BDMATTACHMENT)
          && bizObjAssociationDtls.bizObjectID != CuramConst.kLongZero) {

          BDMFEAttachmentLink bdmfeAttachmentLink =
            BDMFEAttachmentLinkFactory.newInstance();

          BDMFEAttachmentLinkKeyStruct3 attachmentLinkKeyStruct3 =
            new BDMFEAttachmentLinkKeyStruct3();
          attachmentLinkKeyStruct3.attachmentID =
            bizObjAssociationDtls.bizObjectID;
          attachmentLinkKeyStruct3.recordStatus = RECORDSTATUS.NORMAL;
          attachmentLinkKeyStruct3.attachmentLinkTypCd =
            BDMATTACHMENTLINKTYPE.FAATTACHMENTLINK;

          BDMFEAttachmentLinkDtlsList attachmentLinkDtlsList =
            bdmfeAttachmentLink
              .readByAttachmentIDAttchmtLnkTypCdAndRecordStatus(
                attachmentLinkKeyStruct3);

          for (BDMFEAttachmentLinkDtls bdmfeAttachmentLinkDtls : attachmentLinkDtlsList.dtls) {

            fApplicationID = bdmfeAttachmentLinkDtls.relatedID;

            updateFAStatus(fApplicationID, faStatusCode);

          }

        } else if (bizObjAssociationDtls.bizObjectType
          .equals(BUSINESSOBJECTTYPE.BDMFOREIGNAPPLICATION)
          && bizObjAssociationDtls.bizObjectID != CuramConst.kLongZero) {

          fApplicationID = bizObjAssociationDtls.bizObjectID;

          updateFAStatus(fApplicationID, faStatusCode);

        }

      }

    }

  }

  /**
   * This is a utility method to update the status of foreign application.
   *
   * @author hamed.mohammed
   * @param fApplicationID
   * @throws AppException
   * @throws InformationalException
   */
  public static void updateFAStatus(long fApplicationID, String faStatusCode)
    throws AppException, InformationalException {

    if (fApplicationID != CuramConst.kLongZero) {

      BDMForeignApplicationDtls bdmForeignApplicationDtls = null;
      BDMForeignApplicationKey bdmForeignApplicationKey = null;

      bdmForeignApplicationKey = new BDMForeignApplicationKey();
      bdmForeignApplicationKey.fApplicationID = fApplicationID;

      bdmForeignApplicationDtls = new BDMForeignApplicationDtls();

      bdmForeignApplicationDtls = BDMForeignApplicationFactory.newInstance()
        .read(bdmForeignApplicationKey);

      bdmForeignApplicationDtls.status = faStatusCode;

      BDMForeignApplicationFactory.newInstance()
        .modify(bdmForeignApplicationKey, bdmForeignApplicationDtls);

    }

  }

}
