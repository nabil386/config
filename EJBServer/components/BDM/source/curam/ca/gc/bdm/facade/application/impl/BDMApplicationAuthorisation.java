package curam.ca.gc.bdm.facade.application.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.message.BDMAPPLICATIONCASE;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.VERIFICATIONSTATUS;
import curam.commonintake.authorisation.facade.impl.ApplicationAuthorisation;
import curam.commonintake.authorisation.facade.struct.AuthorisationDetails;
import curam.core.sl.entity.struct.CaseKeyStruct;
import curam.core.sl.infrastructure.entity.fact.EvidenceDescriptorFactory;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtls;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.verification.sl.infrastructure.fact.VerificationFactory;
import curam.verification.sl.infrastructure.intf.Verification;
import curam.verification.sl.infrastructure.struct.CaseEvidenceVerificationDetails;
import curam.verification.sl.infrastructure.struct.OutstandingIndicator;

public class BDMApplicationAuthorisation extends
  curam.ca.gc.bdm.facade.application.base.BDMApplicationAuthorisation {

  @Inject
  ApplicationAuthorisation applicationAuthorisation;

  public BDMApplicationAuthorisation() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Override
  public void authorise(final AuthorisationDetails details)
    throws AppException, InformationalException {

    final Verification verificationObj = VerificationFactory.newInstance();

    final OutstandingIndicator outstandingIndicator =
      new OutstandingIndicator();

    outstandingIndicator.verificationStatus = VERIFICATIONSTATUS.NOTVERIFIED;

    final CaseKeyStruct caseKeyStruct = new CaseKeyStruct();

    caseKeyStruct.caseID = details.applicationCaseID;

    final curam.verification.sl.infrastructure.struct.CaseEvidenceVerificationDetailsList caseEvidenceVerificationDetailsList =
      verificationObj.listCaseVerificationDetails(caseKeyStruct,
        outstandingIndicator);

    for (final CaseEvidenceVerificationDetails dtls : caseEvidenceVerificationDetailsList.dtls) {

      final EvidenceDescriptorKey key = new EvidenceDescriptorKey();
      key.evidenceDescriptorID = dtls.evidenceDescriptorID;
      final EvidenceDescriptorDtls evidenceDescriptorDtls =
        EvidenceDescriptorFactory.newInstance().read(key);
      if (!evidenceDescriptorDtls.evidenceType
        .equals(CASEEVIDENCE.BDMDEPENDANT)) {
        throw new AppException(BDMAPPLICATIONCASE.ERR_AUTH_VALIDATION);
      }

    }
    applicationAuthorisation.authorise(details);
  }

}
