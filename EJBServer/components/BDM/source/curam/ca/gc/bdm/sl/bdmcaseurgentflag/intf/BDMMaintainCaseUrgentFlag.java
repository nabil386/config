package curam.ca.gc.bdm.sl.bdmcaseurgentflag.intf;

import curam.ca.gc.bdm.facade.bdmcaseurgentflag.struct.BDMCaseUrgentFlagDetails;
import curam.ca.gc.bdm.facade.bdmcaseurgentflag.struct.BDMCaseUrgentFlagDetailsList;
import curam.ca.gc.bdm.facade.bdmcaseurgentflag.struct.BDMCaseUrgentFlagIDKey;
import curam.core.facade.struct.CaseIDDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

public interface BDMMaintainCaseUrgentFlag {

  BDMCaseUrgentFlagDetailsList listCurrentUrgentFlags(CaseIDDetails var1)
    throws AppException, InformationalException;

  BDMCaseUrgentFlagDetailsList listPreviousUrgentFlags(CaseIDDetails var1)
    throws AppException, InformationalException;

  BDMCaseUrgentFlagDetails readCaseUrgentFlag(BDMCaseUrgentFlagIDKey var1)
    throws AppException, InformationalException;

  void modifyCaseUrgentFlag(BDMCaseUrgentFlagDetails var1)
    throws AppException, InformationalException;

  void createCaseUrgentFlag(BDMCaseUrgentFlagDetails var1)
    throws AppException, InformationalException;

  void deleteCaseUrgentFlag(BDMCaseUrgentFlagIDKey var1)
    throws AppException, InformationalException;

  void validateCreateCaseUrgentFlag(BDMCaseUrgentFlagDetails var1)
    throws AppException, InformationalException;

  void validateModifyCaseUrgentFlag(BDMCaseUrgentFlagDetails var1)
    throws AppException, InformationalException;
}
