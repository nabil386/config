package curam.ca.gc.bdm.communication.impl;

import com.google.inject.Inject;
import curam.attachment.impl.Attachment;
import curam.core.impl.EnvVars;
import curam.core.sl.entity.fact.DocumentTemplateFactory;
import curam.core.sl.entity.struct.DocumentTemplateDtls;
import curam.core.sl.entity.struct.DocumentTemplateKey;
import curam.core.sl.struct.MSWordCommunicationDetails1;
import curam.core.sl.struct.TemplateDataDetails;
import curam.core.sl.struct.WordTemplateDocumentAndDataDetails;
import curam.core.struct.AttachmentDtls;
import curam.core.struct.AttachmentKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Configuration;
import curam.util.type.Blob;
import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.POIXMLProperties;
import org.apache.poi.POIXMLProperties.CustomProperties;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFSDT;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.officeDocument.x2006.customProperties.CTProperty;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;

public class BDMMSWordDocumentsImpl implements BDMMSWordDocuments {

  @Inject
  private BDMMSWordTemplateData bdmWordTemplateData;

  @Inject
  protected Attachment attachment;

  public BDMMSWordDocumentsImpl() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Get BDM specific MS Word customized data for templates
   *
   * @param Template and Document data key
   * @return Template data details
   */
  @Override
  public WordTemplateDocumentAndDataDetails getWordTemplateDocumentAndData(
    final MSWordCommunicationDetails1 wordCommDetails)
    throws AppException, InformationalException {

    // initialize return , attachment and Word communication key object
    final WordTemplateDocumentAndDataDetails wordTemplateDocumentAndDataDetails =
      new WordTemplateDocumentAndDataDetails();

    final AttachmentKey attachmentKey = new AttachmentKey();

    // Read attachment details
    attachmentKey.attachmentID = wordCommDetails.attachmentID;
    final AttachmentDtls attachmentDtls = attachment.read(attachmentKey);

    // Get the document contents
    wordTemplateDocumentAndDataDetails.document =
      attachmentDtls.attachmentContents;

    // Call local method to retrieve customized template data
    final TemplateDataDetails templateDataDetails =
      getTemplateData(wordCommDetails);

    wordTemplateDocumentAndDataDetails.templateData =
      templateDataDetails.documentData;

    return wordTemplateDocumentAndDataDetails;
  }

  /**
   * Get BDM specific MS Word customized data for templates
   *
   * @param Template and Document data key
   * @return Template data details
   */
  @Override
  public TemplateDataDetails
    getTemplateData(final MSWordCommunicationDetails1 wordCommDetails)
      throws AppException, InformationalException {

    // Create return template data object
    final TemplateDataDetails templateDataDetails =
      bdmWordTemplateData.getCommonTemplateData(wordCommDetails);

    final String freeTextLetterTemplateID =
      Configuration.getProperty(EnvVars.ENV_TEMPLATE_ID_FREE_TEXT_LETTER);
    // TASK 14702 - Usr getFreeFormLetterTemplateData to populate officedetails
    // and other data
    final String returnWarrantForAccountHolder = Configuration
      .getProperty(EnvVars.ENV_TEMPLATE_ID_DDP_RETURN_WARANT_ACCOUNT_HOLDER);
    if (wordCommDetails.templateID.startsWith(freeTextLetterTemplateID)
      || wordCommDetails.templateID
        .startsWith(returnWarrantForAccountHolder)) {

    }

    return templateDataDetails;
  }

  /**
   * update MS Word customProperties for dynamic field value update
   *
   * @param MSWordCommunicationDetails1 wordCommunication Details
   * @return Blob instance of word document file with updated property values
   */
  @Override
  public Blob updateMSWordCustomProperties(
    final MSWordCommunicationDetails1 wordCommDetails)
    throws AppException, InformationalException {

    final WordTemplateDocumentAndDataDetails wordTemplateData =
      getWordTemplateDocumentAndData(wordCommDetails);

    final InputStream is =
      new ByteArrayInputStream(wordTemplateData.document.copyBytes());
    final BufferedInputStream bis = new BufferedInputStream(is);
    final ByteArrayOutputStream os = new ByteArrayOutputStream();
    try {
      final XWPFDocument xwpfDoc = new XWPFDocument(bis);
      // update customProperties on MS Word
      updateXWPFCustomProperties(xwpfDoc, wordCommDetails);
      populateDataToXWPFProperties(xwpfDoc);
      xwpfDoc.write(os);
      final Blob returnBlob = new Blob(os.toByteArray());
      xwpfDoc.close();
      os.close();
      bis.close();
      is.close();
      return returnBlob;
    } catch (final IOException e) {
      e.printStackTrace();
    }
    return Blob.kEmptyBlob;
  }

  /**
   * BDM specific MS Word to pdf conversion
   *
   * @param MSWordCommunicationDetails1 wordCommunication Details
   * @return Blob instance of PDF file
   */
  @Override
  public Blob convertToPDF(final MSWordCommunicationDetails1 wordCommDetails)
    throws AppException, InformationalException {

    final WordTemplateDocumentAndDataDetails wordTemplateData =
      getWordTemplateDocumentAndData(wordCommDetails);

    final InputStream is =
      new ByteArrayInputStream(wordTemplateData.document.copyBytes());
    final BufferedInputStream bis = new BufferedInputStream(is);
    final ByteArrayOutputStream os = new ByteArrayOutputStream();
    try {
      final XWPFDocument xwpfDoc = new XWPFDocument(bis);
      // xwpfDoc.enforceUpdateFields();
      updateXWPFCustomProperties(xwpfDoc, wordCommDetails);
      populateDataToXWPFProperties(xwpfDoc);

      convertXWPFSDTToParagraphElement(xwpfDoc);

      // getXWPDFDocumentRuns(xwpfDoc);

      final PdfOptions pdfOptions = PdfOptions.create();
      PdfConverter.getInstance().convert(xwpfDoc, os, pdfOptions);
      final Blob returnBlob = new Blob(os.toByteArray());
      xwpfDoc.close();
      os.close();
      bis.close();
      is.close();
      return returnBlob;
    } catch (final IOException e) {
      e.printStackTrace();
    }
    return Blob.kEmptyBlob;
  }

  /**
   * read Document template object by documentTemplateID
   *
   * @param String docTemplateID
   * @return DocumentTemplateDtls instance of document template entry
   */
  @Override
  public DocumentTemplateDtls getDocumentTemplateByID(
    final String docTemplateID) throws AppException, InformationalException {

    // read template locale
    final DocumentTemplateKey docTemplateKey = new DocumentTemplateKey();
    docTemplateKey.documentTemplateID = docTemplateID;
    final DocumentTemplateDtls docTemplateDtls =
      DocumentTemplateFactory.newInstance().read(docTemplateKey);
    return docTemplateDtls;
  }

  // Read and move the XWPFDocument SDT elements content to paragraph element
  public void convertXWPFSDTToParagraphElement(final XWPFDocument xwpfDoc) {

    final List<IBodyElement> bodyElms = xwpfDoc.getBodyElements();
    int elementIndex = 0;
    for (final IBodyElement element : bodyElms) {
      if (element instanceof XWPFSDT) {
        final XWPFSDT sdtObj = (XWPFSDT) element;
        final XWPFParagraph paragraph =
          (XWPFParagraph) bodyElms.get(elementIndex - 1);
        final List<XWPFRun> pRuns = paragraph.getRuns();
        if (pRuns.size() > 0)
          pRuns.get(0).setText(sdtObj.getContent().getText());
        else {
          final XWPFRun run = paragraph.createRun();
          run.setText(sdtObj.getContent().getText());
        }
        // xwpfDoc.setParagraph(paragraph, elementIndex);
        // xwpfDoc.removeBodyElement(elementIndex);
        // xwpfDoc.removeBodyElement(
        // xwpfDoc.getPosOfParagraph(xwpfDoc.getLastParagraph()));

        break;
      }
      elementIndex++;
    }
  }

  // update XWPFDocument custom properties value with latest from MS Word
  // communication Details
  public CustomProperties updateXWPFCustomProperties(
    final XWPFDocument xwpfDoc,
    final MSWordCommunicationDetails1 wordCommDetails)
    throws AppException, InformationalException {

    final Map<String, String> propertyMap =
      bdmWordTemplateData.getCommonDataMap(wordCommDetails);

    final POIXMLProperties xmlProp = xwpfDoc.getProperties();
    final CustomProperties customProperties = xmlProp.getCustomProperties();

    final String[] mapKeys = new String[propertyMap.size()];
    propertyMap.keySet().toArray(mapKeys);
    for (final String mapKey : mapKeys) {
      if (!customProperties.contains(mapKey)) {
        customProperties.addProperty(mapKey,
          (String) readMapValueIgnoreCase(propertyMap, mapKey));
      } else {
        customProperties.getProperty(mapKey)
          .setLpwstr((String) readMapValueIgnoreCase(propertyMap, mapKey));
      }
    }
    return customProperties;
  }

  // populate XWPFDocument properties with dynamic value from client data
  public void populateDataToXWPFProperties(final XWPFDocument xwpfDoc)
    throws AppException, InformationalException {

    final POIXMLProperties xmlProp = xwpfDoc.getProperties();
    final CustomProperties customProperties = xmlProp.getCustomProperties();
    final Map<String, XWPFRun[]> docPropertyKeyValueMap =
      getXWPDFDocumentRuns(xwpfDoc);
    final String[] ctrMapKeys = new String[docPropertyKeyValueMap.size()];
    docPropertyKeyValueMap.keySet().toArray(ctrMapKeys);
    for (final String ctrKey : ctrMapKeys) {
      final String docPropertyMegaFormat = ctrKey;
      if (docPropertyMegaFormat.contains(BDMXWPFConstants.kDocProperty)) {
        final int startIndex =
          docPropertyMegaFormat.indexOf(BDMXWPFConstants.kDocProperty)
            + BDMXWPFConstants.kDocProperty.length();
        final int endIndex = docPropertyMegaFormat
          .indexOf(BDMXWPFConstants.kDocPropertyDelimiter);
        final String customPropertyName =
          docPropertyMegaFormat.substring(startIndex, endIndex).trim();
        final CTProperty[] ctPropertyArray =
          customProperties.getUnderlyingProperties().getPropertyArray();
        CTProperty ctProperty = null;
        for (final CTProperty ctProp : ctPropertyArray) {
          if (ctProp.getName().equalsIgnoreCase(customPropertyName)) {
            ctProperty = ctProp;
            break;
          }
        }
        final CTR propertyValueCTR =
          docPropertyKeyValueMap.get(ctrKey)[0].getCTR();
        final CTText textNode = propertyValueCTR.getTArray()[0];
        textNode.setStringValue(ctProperty.getLpwstr());
        propertyValueCTR.setTArray(0, textNode);
      }

    }
  }

  public Map<String, XWPFRun[]>
    getXWPDFDocumentRuns(final XWPFDocument xwpfDoc) {

    final Map<String, XWPFRun[]> docPropertyRunMap =
      new HashMap<String, XWPFRun[]>();

    // get all DOCPRoperty value reference from BodyElements
    final List<IBodyElement> bodyElms = xwpfDoc.getBodyElements();
    for (final IBodyElement element : bodyElms) {
      if (element instanceof XWPFParagraph) {
        final XWPFParagraph paragraph = (XWPFParagraph) element;
        final List<XWPFRun> pRuns = paragraph.getRuns();
        final Map<String, XWPFRun[]> propValueMap =
          getDocPropertyAndValueMap(pRuns);
        docPropertyRunMap.putAll(propValueMap);
      } else if (element instanceof XWPFTable) {
        final XWPFTable table = (XWPFTable) element;
        final List<XWPFTableRow> tRows = table.getRows();
        for (final XWPFTableRow row : tRows) {
          final List<XWPFTableCell> tCells = row.getTableCells();
          for (final XWPFTableCell cell : tCells) {
            final List<XWPFParagraph> paragraphs = cell.getParagraphs();
            for (final XWPFParagraph paragraph : paragraphs) {
              final List<XWPFRun> pRuns = paragraph.getRuns();
              final Map<String, XWPFRun[]> propValueMap =
                getDocPropertyAndValueMap(pRuns);
              docPropertyRunMap.putAll(propValueMap);

            }
          }
        }
      }
    }

    // get all DOCPRoperty value reference from header elements
    final List<XWPFHeader> headers = xwpfDoc.getHeaderList();
    for (final XWPFHeader header : headers) {
      final List<IBodyElement> headerElms = header.getBodyElements();
      for (final IBodyElement element : headerElms) {
        if (element instanceof XWPFParagraph) {
          final XWPFParagraph paragraph = (XWPFParagraph) element;
          final List<XWPFRun> pRuns = paragraph.getRuns();
          final Map<String, XWPFRun[]> propValueMap =
            getDocPropertyAndValueMap(pRuns);
          docPropertyRunMap.putAll(propValueMap);
        } else if (element instanceof XWPFTable) {
          final XWPFTable table = (XWPFTable) element;
          final List<XWPFTableRow> tRows = table.getRows();
          for (final XWPFTableRow row : tRows) {
            final List<XWPFTableCell> tCells = row.getTableCells();
            for (final XWPFTableCell cell : tCells) {
              final List<XWPFParagraph> paragraphs = cell.getParagraphs();
              for (final XWPFParagraph paragraph : paragraphs) {
                final List<XWPFRun> pRuns = paragraph.getRuns();
                final Map<String, XWPFRun[]> propValueMap =
                  getDocPropertyAndValueMap(pRuns);
                docPropertyRunMap.putAll(propValueMap);

              }
            }
          }
        }
      }
    }
    // get all DOCPRoperty value reference from footer elements
    final List<XWPFFooter> footers = xwpfDoc.getFooterList();
    for (final XWPFFooter footer : footers) {
      final List<IBodyElement> headerElms = footer.getBodyElements();
      for (final IBodyElement element : headerElms) {
        if (element instanceof XWPFParagraph) {
          final XWPFParagraph paragraph = (XWPFParagraph) element;
          final List<XWPFRun> pRuns = paragraph.getRuns();
          final Map<String, XWPFRun[]> propValueMap =
            getDocPropertyAndValueMap(pRuns);
          docPropertyRunMap.putAll(propValueMap);
        } else if (element instanceof XWPFTable) {
          final XWPFTable table = (XWPFTable) element;
          final List<XWPFTableRow> tRows = table.getRows();
          for (final XWPFTableRow row : tRows) {
            final List<XWPFTableCell> tCells = row.getTableCells();
            for (final XWPFTableCell cell : tCells) {
              final List<XWPFParagraph> paragraphs = cell.getParagraphs();
              for (final XWPFParagraph paragraph : paragraphs) {
                final List<XWPFRun> pRuns = paragraph.getRuns();
                final Map<String, XWPFRun[]> propValueMap =
                  getDocPropertyAndValueMap(pRuns);
                docPropertyRunMap.putAll(propValueMap);

              }
            }
          }
        }
      }
    }
    return docPropertyRunMap;
  }

  // get the count of XWPFRun that contains the FldChar 'begin' from the list of
  // XWPFRun and return the count as int
  public int getFldCharBeginCount(final List<XWPFRun> runs) {

    int count = 0;
    for (final XWPFRun run : runs) {
      final CTR ctr = run.getCTR();
      if (ctr.getFldCharArray().length > 0 && ctr.getFldCharArray(0)
        .getFldCharType().toString().equals(BDMXWPFConstants.kbegin))
        count++;
    }
    return count;
  }

  // get the array of XWPFRun cursor that contains the FldChar 'begin' from the
  // list of XWPFRun
  public XWPFRun[] getFldCharBeginCursorArray(final List<XWPFRun> runs) {

    final int size = getFldCharBeginCount(runs);
    final XWPFRun[] beginRunCursors = new XWPFRun[size];
    int index = 0;
    for (final XWPFRun run : runs) {
      final CTR ctr = run.getCTR();
      if (ctr.getFldCharArray().length > 0 && ctr.getFldCharArray(0)
        .getFldCharType().toString().equals(BDMXWPFConstants.kbegin)) {
        beginRunCursors[index] = run;
        index++;
      }
    }
    return beginRunCursors;
  }

  // get the instance of XWPFRun cursor that contains the FldChar 'separate'
  // from the list of XWPFRun with the beginning cursor
  public XWPFRun getFldCharSeparateCursor(final List<XWPFRun> runs,
    final XWPFRun beginRunCursor) {

    final int beginIndex = runs.indexOf(beginRunCursor);
    for (int i = beginIndex; i < runs.size(); i++) {
      final CTR ctr = runs.get(i).getCTR();
      if (ctr.getFldCharArray().length > 0 && ctr.getFldCharArray(0)
        .getFldCharType().toString().equals(BDMXWPFConstants.kseparate)) {
        return runs.get(i);
      }
    }
    return null;
  }

  // get the subset of XWPFRun array between the FldChar 'begin' and the FldChar
  // 'separate' and return the subset of XWPFRun array.
  public XWPFRun[] getCustomPropertyRunsSubArray(final List<XWPFRun> runs,
    final XWPFRun currentCursor) {

    final int beginIndex = runs.indexOf(currentCursor);
    final ArrayList<XWPFRun> propertyRuns = new ArrayList<XWPFRun>();
    for (int i = beginIndex; i < runs.size(); i++) {
      final CTR ctr = runs.get(i).getCTR();
      if (ctr.getFldCharArray().length > 0 && ctr.getFldCharArray(0)
        .getFldCharType().toString().equals(BDMXWPFConstants.kbegin)) {
        continue;
      }
      if (ctr.getFldCharArray().length > 0 && ctr.getFldCharArray(0)
        .getFldCharType().toString().equals(BDMXWPFConstants.kseparate)) {
        break;
      }
      propertyRuns.add(runs.get(i));
    }
    final XWPFRun[] returnRuns = new XWPFRun[propertyRuns.size()];
    propertyRuns.toArray(returnRuns);
    return returnRuns;
  }

  public XWPFRun[] getCustomPropertyValueNode(final List<XWPFRun> runs,
    final XWPFRun run) {

    final int beginIndex = runs.indexOf(run);
    final ArrayList<XWPFRun> propertyRuns = new ArrayList<XWPFRun>();
    for (int i = beginIndex; i < runs.size(); i++) {
      final CTR ctr = runs.get(i).getCTR();
      if (ctr.getFldCharArray().length > 0 && ctr.getFldCharArray(0)
        .getFldCharType().toString().equals(BDMXWPFConstants.kseparate)) {
        continue;
      }

      if (ctr.getFldCharArray().length > 0 && ctr.getFldCharArray(0)
        .getFldCharType().toString().equals(BDMXWPFConstants.kend)) {
        break;
      } else {
        propertyRuns.add(runs.get(i));
      }

    }
    final XWPFRun[] returnRuns = new XWPFRun[propertyRuns.size()];
    propertyRuns.toArray(returnRuns);
    return returnRuns;
  }

  // get all instr instance from the XWPFRun array and return the appended
  // string value of the instr set
  public String getCombinedInstrValue(final XWPFRun[] runs) {

    String instrValue = "";
    for (final XWPFRun run : runs) {
      final CTR instrCTR = run.getCTR();
      for (final CTText instrText : instrCTR.getInstrTextArray()) {
        instrValue += instrText.getStringValue();
      }
    }
    return instrValue;
  }

  // get the mapping of the DOCPROPERTY string and the display XPWFRun isntance
  // from the list of runs
  public Map<String, XWPFRun[]>
    getDocPropertyAndValueMap(final List<XWPFRun> runs) {

    final Map<String, XWPFRun[]> docPropertyRunMap =
      new HashMap<String, XWPFRun[]>();
    final XWPFRun[] beginRuns = getFldCharBeginCursorArray(runs);

    for (final XWPFRun run : beginRuns) {
      final XWPFRun[] beginFldCharNode =
        getCustomPropertyRunsSubArray(runs, run);
      final String docPropertyTextValue =
        getCombinedInstrValue(beginFldCharNode);
      final XWPFRun separatRunNode = getFldCharSeparateCursor(runs, run);

      // BUG 26305 Resolve UHSE
      if (null != separatRunNode) {
        final XWPFRun[] endFldCharNode =
          getCustomPropertyValueNode(runs, separatRunNode);

        docPropertyRunMap.put(docPropertyTextValue, endFldCharNode);
      }
    }

    return docPropertyRunMap;
  }

  // read map key with string type with non-case sensitive and its value pair
  public Object readMapValueIgnoreCase(final Map<String, ?> stringMap,
    final String key) {

    final String[] stringMapKeys = new String[stringMap.size()];
    stringMap.keySet().toArray(stringMapKeys);
    for (final String stringMapKey : stringMapKeys) {
      if (stringMapKey.equalsIgnoreCase(key))
        return stringMap.get(stringMapKey);
    }

    return null;
  }
}
