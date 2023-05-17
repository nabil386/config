package curam.ca.gc.bdm.correspondenceframework.impl;

import curam.ca.gc.bdm.message.BDMBPOCCT;
import curam.ca.gc.bdm.message.BDMCORRESPONDENCEFRAMEWORK;
import curam.codetable.LOCALE;
import curam.util.exception.AppException;
import curam.util.exception.InformationalElement;
import curam.util.exception.InformationalException;
import curam.util.exception.InformationalManager;
import curam.util.exception.LocalisableString;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Money;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BDMCorrespondenceValidationDefaultHandler
  implements BDMCorrespondenceValidationInterface {

  /**
   * This method validates the overall context before getting the data
   *
   * @param configDtls
   * @param pojoObject
   * @return
   * @throws AppException
   * @throws InformationalException
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   */
  @Override
  public InformationalManager validateTemplateContext(
    final curam.ca.gc.bdm.correspondenceframework.impl.BDMLetterTemplateConfig configDtls,
    final BDMCorrespondenceRecipientClientInput correspondenceRecipientClientInput,
    final InformationalManager informationalManager)
    throws AppException, InformationalException {

    return informationalManager;
  }

  /**
   * This method validates the data
   *
   * @param configDtls
   * @param pojoObject
   * @return
   * @throws AppException
   * @throws InformationalException
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   */
  @Override
  public InformationalManager validateData(
    final curam.ca.gc.bdm.correspondenceframework.impl.BDMLetterTemplateConfig configDtls,
    final Object pojoObject,
    final BDMCorrespondenceRecipientClientInput correspondenceRecipientClientInput,
    final InformationalManager informationalManager)
    throws AppException, InformationalException {

    final List<String> missingFields = new ArrayList<>();

    // Perform mandatory checks
    if (pojoObject == null) {
      // If this is null then it means that there is no data
      for (final BDMLetterTemplateMandatoryFieldConfig fieldConfig : configDtls.mandatoryFieldsList) {

        if (TransactionInfo.getProgramLocale()
          .equalsIgnoreCase(LOCALE.ENGLISH)) {
          missingFields.add(fieldConfig.englishText);
        } else {
          missingFields.add(fieldConfig.frenchText);
        }
      }
    } else {

      final Class<?> clazz = pojoObject.getClass();

      final Map<java.lang.String, Object> fieldElementsMap = new HashMap<>();
      for (final Field field : clazz.getDeclaredFields()) {

        try {
          fieldElementsMap.put(field.getName(), field.get(pojoObject));
        } catch (final IllegalArgumentException | IllegalAccessException e) {
          e.printStackTrace();
          throw new AppException(
            BDMBPOCCT.ERR_UNABLE_TO_CREATE_CORRESPONDENCE, e);
        }
      }
      boolean missingFieldInd = false;
      for (final BDMLetterTemplateMandatoryFieldConfig fieldConfig : configDtls.mandatoryFieldsList) {
        missingFieldInd = false;
        if (fieldElementsMap.containsKey(fieldConfig.fieldName)
          && fieldElementsMap.get(fieldConfig.fieldName) != null) {

          // If the data type is Money
          try {
            final Field field =
              pojoObject.getClass().getField(fieldConfig.fieldName);
            final Class<?> typeClass = field.getType();
            if (typeClass.getName().equalsIgnoreCase(Money.class.getName())) {
              final Money money = (Money) field.get(pojoObject);
              if (money.isZero()) {
                missingFieldInd = true;
              }
            } else if (fieldElementsMap.get(fieldConfig.fieldName).toString()
              .isEmpty()) {
              missingFieldInd = true;
            }

          } catch (final IllegalArgumentException | IllegalAccessException
            | NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
            throw new AppException(
              BDMBPOCCT.ERR_UNABLE_TO_CREATE_CORRESPONDENCE, e);
          }

          if (missingFieldInd) {

            if (TransactionInfo.getProgramLocale()
              .equalsIgnoreCase(LOCALE.ENGLISH)) {
              missingFields.add(fieldConfig.englishText);
            } else {
              missingFields.add(fieldConfig.frenchText);
            }
          }
        }
      }

    }
    if (!missingFields.isEmpty()
      && !missingFields.toString().equalsIgnoreCase("[]")) {

      final LocalisableString localisableString = new LocalisableString(
        BDMCORRESPONDENCEFRAMEWORK.ERR_MANDATORY_FIELDS_MISSING);
      localisableString.arg(missingFields.toString());
      informationalManager.addInformationalMsg(localisableString, "",
        InformationalElement.InformationalType.kError);

    }
    return informationalManager;
  }
}
