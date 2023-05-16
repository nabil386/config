/**
 *
 */
package curam.ca.gc.bdm.facade.bdmcommonintake.impl;

import curam.ca.gc.bdm.entity.bdmcommonintake.fact.BDMProgramReopenInfoFactory;
import curam.ca.gc.bdm.entity.bdmcommonintake.intf.BDMProgramReopenInfo;
import curam.ca.gc.bdm.entity.bdmcommonintake.struct.BDMProgramReopenInfoDtls;
import curam.ca.gc.bdm.entity.bdmcommonintake.struct.BDMProgramReopenInfoKey;
import curam.ca.gc.bdm.facade.bdmcommonintake.struct.BDMProgramReopenDetails;
import curam.ca.gc.bdm.facade.bdmcommonintake.struct.BDMProgramReopenDetailsList;
import curam.commonintake.facade.fact.ApplicationCaseProgramFactory;
import curam.commonintake.facade.intf.ApplicationCaseProgram;
import curam.commonintake.facade.struct.ProgramReopenDetails;
import curam.commonintake.facade.struct.ProgramReopenDetailsList;
import curam.commonintake.facade.struct.ProgramReopenKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.workspaceservices.intake.struct.IntakeProgramApplicationKey;

/**
 * @author amod.gole
 *
 * TASK-28625 Class Customized to list ProgramReopenInfo to display Datetime for
 * reopenDate
 *
 */
public class BDMApplicationCaseProgram extends
  curam.ca.gc.bdm.facade.bdmcommonintake.base.BDMApplicationCaseProgram {

  @Override
  public void reopen(final ProgramReopenKey key)
    throws AppException, InformationalException {

    final ApplicationCaseProgram applicationCaseProgram =
      ApplicationCaseProgramFactory.newInstance();
    applicationCaseProgram.reopen(key);

  }

  @Override
  public BDMProgramReopenDetailsList
    listProgramReopenings(final IntakeProgramApplicationKey key)
      throws AppException, InformationalException {

    final ApplicationCaseProgram applicationCaseProgram =
      ApplicationCaseProgramFactory.newInstance();
    final BDMProgramReopenInfo bdmProgramReopenInfo =
      BDMProgramReopenInfoFactory.newInstance();
    final ProgramReopenDetailsList programReopenDetailsList =
      applicationCaseProgram.listProgramReopenings(key);
    final BDMProgramReopenDetailsList bdmProgramReopenDetailsList =
      new BDMProgramReopenDetailsList();
    for (final ProgramReopenDetails programReopenDetails : programReopenDetailsList.list) {
      final BDMProgramReopenDetails bdmProgramReopenDetails =
        new BDMProgramReopenDetails();
      bdmProgramReopenDetails.dtls.assign(programReopenDetails);
      final BDMProgramReopenInfoKey bdmProgramReopenInfoKey =
        new BDMProgramReopenInfoKey();
      bdmProgramReopenInfoKey.programReopenInfoID =
        programReopenDetails.programReopenInfoID;
      final BDMProgramReopenInfoDtls bdmProgramReopenInfoDtls =
        bdmProgramReopenInfo.read(bdmProgramReopenInfoKey);
      bdmProgramReopenDetails.reopenDateTime =
        bdmProgramReopenInfoDtls.reopenDateTime;

      bdmProgramReopenDetailsList.list.add(bdmProgramReopenDetails);
    }
    return bdmProgramReopenDetailsList;

  }

}
