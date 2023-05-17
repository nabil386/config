
package curam.ca.gc.bdm.correspondenceframework.impl;

import java.util.ArrayList;
import java.util.List;

public class BDMLetterTemplateConfig {

  public String templateName = "";

  public boolean enabledInd = true;

  public boolean shouldUpdateInd = true;

  public String sql = "";

  public String adhocSQL = "";

  public String fileName = "";

  public String filePath = "";

  public String inputClassName = "";

  public String outputClassName = "";

  public String validationHandlerClassName = "";

  public String dataHandlerClassName = "";

  public List<BDMLetterTemplateMandatoryFieldConfig> mandatoryFieldsList =
    new ArrayList<BDMLetterTemplateMandatoryFieldConfig>();
}
