/**
 *
 */
package curam.ca.gc.bdm.hooks.task.impl;

import curam.ca.gc.bdm.sl.bdminbox.struct.BDMTaskQueryResultDetailsList;
import curam.ca.gc.bdm.sl.taskquery.struct.BDMTaskQueryCriteria;
import curam.common.util.xml.dom.Document;
import curam.common.util.xml.dom.Element;
import curam.common.util.xml.dom.input.SAXBuilder;
import curam.common.util.xml.dom.output.XMLOutputter;
import curam.common.util.xml.dom.xpath.XPath;
import curam.core.hook.task.impl.SearchTaskUtilities;
import curam.core.sl.entity.struct.QueryDtls;
import curam.core.sl.struct.ReadMultiOperationDetails;
import curam.message.BPOQUERY;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.XMLParserCache;
import java.io.ByteArrayInputStream;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * Customized the OOTB search task utilities class { {@link SearchTaskUtilities
 * }
 *
 * @author donghua.jin
 *
 */
public class BDMSearchTaskUtilities {

  /**
   * The XPath search separator.
   */
  protected static final String XPATH_NODE_SEPARATOR = "/";

  /**
   * The XPath search start text.
   */
  protected static final String XPATH_START = "//";

  /**
   * The XPath query element.
   */
  protected static final String QUERY = "Query";

  /**
   * The XPath text method.
   */
  protected static final String XPATH_TEXT = "text()";

  /**
   * The XPath urgent flag node.
   */
  protected static final String URGENTFLAG = "UrgentFlag";

  /**
   * The XPath escalation level node.
   */
  protected static final String ESCALATIONLEVEL = "EscalationLevel";

  /**
   * The XPath task type node.
   */
  protected static final String TASKTYPE = "TaskType";

  /**
   * Customized super class's formatXMLQueryCriteria
   * to add the customized search criteria.
   *
   * @see #formatXMLQueryCriteria(TaskQueryCriteria)
   *
   * @param details
   * @return
   * @throws AppException
   */
  public static String formatXMLQueryCriteria(
    final BDMTaskQueryCriteria details) throws AppException {

    String xmlStr =
      SearchTaskUtilities.formatXMLQueryCriteria(details.criteria);

    if (details.caseUrgentFlagTypeCode.length() != 0
      || details.escalationLevel.length() != 0
      || details.taskType.length() != 0) {

      try {
        final SAXBuilder sb = XMLParserCache.getSAXBuilderDisallowAllDTD();
        final Document doc =
          sb.build(new InputSource(new StringReader(xmlStr)));

        final XPath xpath = new XPath(XPATH_START + QUERY);
        final Element queryElement =
          (Element) xpath.selectSingleDOMNode(doc.getRootElement());

        if (details.caseUrgentFlagTypeCode.length() != 0) {
          final Element urgFlag = new Element(URGENTFLAG);
          urgFlag.setText(details.caseUrgentFlagTypeCode);
          queryElement.addContent(urgFlag);
        }

        if (details.escalationLevel.length() != 0) {
          final Element urgFlag = new Element(ESCALATIONLEVEL);
          urgFlag.setText(details.escalationLevel);
          queryElement.addContent(urgFlag);
        }

        if (details.taskType.length() != 0) {
          final Element taskType = new Element(TASKTYPE);
          taskType.setText(details.taskType);
          queryElement.addContent(taskType);
        }

        xmlStr = new XMLOutputter().outputString(doc.getRootElement());

      } catch (final Exception e) {
        e.printStackTrace();
        final AppException appEx =
          new AppException(BPOQUERY.ERR_QUERY_FAILED_TO_PARSE_SAVED_QUERY);

        throw appEx;
      }

    }

    return xmlStr;
  }

  /**
   * Customized super class's getTaskQueryCriteria
   * to call customized parse criteria method.
   *
   * @see #getTaskQueryCriteria(QueryDtls)
   */
  public static BDMTaskQueryCriteria getTaskQueryCriteria(
    final QueryDtls details) throws AppException, InformationalException {

    final BDMTaskQueryCriteria bdmTaskQueryCriteria =
      parseXMLQueryCriteria(details.query);

    bdmTaskQueryCriteria.criteria.queryName = details.queryName;

    return bdmTaskQueryCriteria;
  }

  /**
   * Customized super class's parseXMLQueryCriteria
   * to parse the customized search criteria.
   *
   * @see #parseXMLQueryCriteria(String)
   */
  public static BDMTaskQueryCriteria parseXMLQueryCriteria(
    final String xmlCriteria) throws AppException, InformationalException {

    final BDMTaskQueryCriteria bdmTaskQueryCriteria =
      new BDMTaskQueryCriteria();

    bdmTaskQueryCriteria.criteria =
      SearchTaskUtilities.parseXMLQueryCriteria(xmlCriteria);

    try {
      final DocumentBuilderFactory factory =
        DocumentBuilderFactory.newInstance();
      final DocumentBuilder builder = factory.newDocumentBuilder();

      final ByteArrayInputStream input =
        new ByteArrayInputStream(xmlCriteria.getBytes("UTF-8"));
      final org.w3c.dom.Document doc = builder.parse(input);

      final javax.xml.xpath.XPath xPath =
        XPathFactory.newInstance().newXPath();
      String expression = XPATH_START + QUERY + XPATH_NODE_SEPARATOR
        + URGENTFLAG + XPATH_NODE_SEPARATOR + XPATH_TEXT;
      NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(doc,
        XPathConstants.NODESET);

      if (nodeList.getLength() > 0) {
        bdmTaskQueryCriteria.caseUrgentFlagTypeCode =
          nodeList.item(0).getTextContent();
      }

      expression = XPATH_START + QUERY + XPATH_NODE_SEPARATOR
        + ESCALATIONLEVEL + XPATH_NODE_SEPARATOR + XPATH_TEXT;
      nodeList = (NodeList) xPath.compile(expression).evaluate(doc,
        XPathConstants.NODESET);

      if (nodeList.getLength() > 0) {
        bdmTaskQueryCriteria.escalationLevel =
          nodeList.item(0).getTextContent();
      }

      expression = XPATH_START + QUERY + XPATH_NODE_SEPARATOR + TASKTYPE
        + XPATH_NODE_SEPARATOR + XPATH_TEXT;
      nodeList = (NodeList) xPath.compile(expression).evaluate(doc,
        XPathConstants.NODESET);

      if (nodeList.getLength() > 0) {
        bdmTaskQueryCriteria.taskType = nodeList.item(0).getTextContent();
      }

    } catch (final Exception e) {
      final AppException appEx =
        new AppException(BPOQUERY.ERR_QUERY_FAILED_TO_PARSE_SAVED_QUERY);

      throw appEx;
    }

    return bdmTaskQueryCriteria;
  }

  /**
   * Customized the OOTB method to pull the custom attributes.
   *
   * @param bdmTaskQueryCriteria
   * @param readMultiDetails
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public static BDMTaskQueryResultDetailsList searchTask(
    final BDMTaskQueryCriteria bdmTaskQueryCriteria,
    final ReadMultiOperationDetails readMultiDetails)
    throws AppException, InformationalException {

    final BDMTaskQueryResultDetailsList resultList = new BDMSearchTaskImpl()
      .searchTask(bdmTaskQueryCriteria, readMultiDetails);

    return resultList;
  }
}
