package curam.ca.gc.bdm.sl.attachment.impl;

import com.google.inject.Inject;
import curam.attachment.impl.Attachment;
import curam.codetable.CASETYPECODE;
import curam.codetable.TEMPLATERELATESTO;
import curam.codetable.XSLTEMPLATETYPE;
import curam.core.fact.CaseHeaderFactory;
import curam.core.struct.CaseKey;
import curam.core.struct.CaseTypeCode;
import curam.core.struct.SearchTemplatesByConcernAndTypeResult;
import curam.core.struct.SearchTemplatesKey;
import curam.core.struct.XSLTemplateDetails;
import curam.util.administration.struct.XSLTemplateDetailsList;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;

public class BDMMaintainXSLTemplate
  extends curam.ca.gc.bdm.sl.attachment.base.BDMMaintainXSLTemplate {

  @Inject
  protected Attachment attachment;

  public BDMMaintainXSLTemplate() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /***
   * This method fetches xsltemplates for Application case . This methods checks
   * if current case is Application case and fetched xsltemplate relates to
   * application case
   *
   * @param searchTemplatesIn
   * @return SearchTemplatesByConcernAndTypeResult
   **/
  @Override
  public SearchTemplatesByConcernAndTypeResult
    searchTemplatesByConcernAndType(
      final SearchTemplatesKey searchTemplatesIn)
      throws AppException, InformationalException {

    final SearchTemplatesByConcernAndTypeResult searchTemplatesByConcernAndTypeResult =
      new SearchTemplatesByConcernAndTypeResult();

    // XSL Template Utility Entity, Template Type Key and Template Details List
    final curam.util.xml.intf.XSLTemplateUtility xslTemplateUtilityObj =
      curam.util.xml.fact.XSLTemplateUtilityFactory.newInstance();
    final curam.util.xml.struct.XSLTemplateTypeKey xslTemplateTypeKey =
      new curam.util.xml.struct.XSLTemplateTypeKey();
    XSLTemplateDetailsList xslTemplateDetailsList;

    // Structure to add to the Out Parameter
    curam.core.struct.XSLTemplateDetails xslTemplateDetails;

    // Boolean variable used to indicate TemplateDtls should be returned
    boolean returnTemplateDtls = false;

    // variable to hold each xslTemplateDtls
    curam.util.administration.struct.XSLTemplateDetails xslTemplateDtls;

    // variable to hold each xslTemplateDtls relatesTo value
    String relatesTo;

    xslTemplateTypeKey.templateType = searchTemplatesIn.templateType;

    // Fetch all xsl templates
    xslTemplateDetailsList =
      xslTemplateUtilityObj.getAllTemplatesByType(xslTemplateTypeKey);

    for (int i = 0; i < xslTemplateDetailsList.dtls.size(); i++) {

      returnTemplateDtls = false;

      xslTemplateDtls = xslTemplateDetailsList.dtls.item(i);
      relatesTo = xslTemplateDtls.relatesTo;

      if (searchTemplatesIn.caseID != 0) {

        final curam.core.intf.CaseHeader caseHeaderObj =
          CaseHeaderFactory.newInstance();
        final CaseKey caseKey = new CaseKey();

        caseKey.caseID = searchTemplatesIn.caseID;

        final CaseTypeCode caseTypeCode =
          caseHeaderObj.readCaseTypeCode(caseKey);
        // Check if current case is application case and xsltemplate relatesto
        // Applicationcase
        if (caseTypeCode.caseTypeCode.equals(CASETYPECODE.APPLICATION_CASE)
          || caseTypeCode.caseTypeCode.equals(CASETYPECODE.INTEGRATEDCASE)
          || caseTypeCode.caseTypeCode.equals(CASETYPECODE.PRODUCTDELIVERY)) {
          if (relatesTo.equals(TEMPLATERELATESTO.CASE)) {

            returnTemplateDtls = true;
          }
        }

        // START : BUG 59421 :RFR Pro Forma Templates not showing up under
        // Reconsideration type on Appeals Hearing Case
        if (caseTypeCode.caseTypeCode.equals(CASETYPECODE.APPEAL)) {
          if (relatesTo.equals(XSLTEMPLATETYPE.HEARING)) {

            returnTemplateDtls = true;
          }
        }
        // END : BUG 59421 :RFR Pro Forma Templates not showing up under
        // Reconsideration type on Appeals Hearing Case

      }

      if (returnTemplateDtls) {

        xslTemplateDetails = new XSLTemplateDetails();

        xslTemplateDetails.templateID = xslTemplateDtls.templateID;
        xslTemplateDetails.templateName = xslTemplateDtls.templateName;
        xslTemplateDetails.latestVersion = xslTemplateDtls.latestVersion;
        xslTemplateDetails.checkedOutInd = xslTemplateDtls.checkedOut;
        xslTemplateDetails.checkedOutBy = xslTemplateDtls.checkedOutBy;
        xslTemplateDetails.checkedOutVersion =
          xslTemplateDtls.checkedOutVersion;
        xslTemplateDetails.checkedOutTime = xslTemplateDtls.checkedOutTime;
        xslTemplateDetails.comment = xslTemplateDtls.coComment;
        xslTemplateDetails.editableInd = xslTemplateDtls.editable;

        xslTemplateDetails.localeIdentifier = xslTemplateDtls.locale;

        searchTemplatesByConcernAndTypeResult.xslTemplateDetailsListOut.dtls
          .addRef(xslTemplateDetails);
      }
    }
    return searchTemplatesByConcernAndTypeResult;
  }

}
