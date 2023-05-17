package curam.ca.gc.bdm.batch.bdmprocessdefinitionimportbatch.impl;

import curam.ca.gc.bdm.batch.bdmprocessdefinitionimportbatch.struct.BDMProcessDefinitionDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.message.WORKFLOW;
import curam.util.type.UniqueID;
import java.io.File;
import java.io.FilenameFilter;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * Batch invoked from the data retention ant script to create unique process
 * id's
 * for the workflow definition.
 *
 */
public class BDMProcessDefinitionImportBatch extends
  curam.ca.gc.bdm.batch.bdmprocessdefinitionimportbatch.base.BDMProcessDefinitionImportBatch {

  @Override
  public void process(final BDMProcessDefinitionDetails details)
    throws AppException, InformationalException {

    final String workflowDirectory = details.workflowDirectory;

    final File dir = new File(workflowDirectory);

    if (!dir.exists() && !dir.isDirectory()) {
      throw new IllegalArgumentException(workflowDirectory);
    }

    final FilenameFilter filenameFilter = new FilenameFilter() {

      @Override
      public boolean accept(final File dir, final String name) {

        return name.toLowerCase().endsWith(".xml")
          && name.toLowerCase().contains("_v");
      }

    };

    final File[] processDefinitions = dir.listFiles(filenameFilter);

    for (final File processDefinition : processDefinitions) {
      updateProcessID(processDefinition);
    }

  }

  private void updateProcessID(final File processDefinition)
    throws AppException {

    try {
      final DocumentBuilderFactory builderFactory =
        DocumentBuilderFactory.newInstance();
      builderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING,
        false);

      final Document workflow =
        builderFactory.newDocumentBuilder().parse(processDefinition);

      final NodeList nodes =
        workflow.getElementsByTagName("workflow-process");
      final Element workflowElement = (Element) nodes.item(0);

      final long newId = UniqueID.nextUniqueID("BDMWKFLW");

      workflowElement.setAttribute("id", String.valueOf(newId));

      final TransformerFactory transformerFactory =
        TransformerFactory.newInstance();
      final Transformer transformer = transformerFactory.newTransformer();
      final DOMSource source = new DOMSource(workflow);
      final StreamResult result = new StreamResult(processDefinition);
      transformer.transform(source, result);

    } catch (final Exception e) {
      final AppException ae =
        new AppException(WORKFLOW.ID_ERROR_CREATING_PROCESS_DEFINITION);
      ae.arg(processDefinition.getName());
      ae.arg(e.getMessage());
      throw ae;
    }

  }

}
