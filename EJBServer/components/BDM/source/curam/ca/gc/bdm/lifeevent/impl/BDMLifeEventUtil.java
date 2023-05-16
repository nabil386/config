package curam.ca.gc.bdm.lifeevent.impl;

import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.type.impl.DynamicEvidenceTypeConverter;
import curam.util.exception.AppRuntimeException;
import curam.util.resources.StringUtil;
import curam.util.resources.Trace;
import curam.util.type.Date;
import curam.util.type.DateTime;
import curam.workspaceservices.util.impl.DateTimeTools;
import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * @since
 *
 * Utility class for the BDM life events.
 *
 */
public class BDMLifeEventUtil {

  /**
   * This method assigns the evidence data from the evidence POJO to the dynamic
   * evidence data map.
   *
   * @param evidenceDetails evidence details POJO
   * @return evidence data map
   */
  public HashMap<String, String>
    getEvidenceData(final Object evidenceDetails) {

    final HashMap<String, String> evidenceData =
      new HashMap<String, String>();

    for (final Field field : evidenceDetails.getClass().getDeclaredFields()) {
      field.setAccessible(true);

      try {

        if (field.getName().equals("evidenceID")
          || field.get(evidenceDetails) == null
          || StringUtil.isNullOrEmpty(field.get(evidenceDetails).toString())
          || field.isSynthetic()) {
          continue;
        }

        if (field.getType().isAssignableFrom(Date.class)) {

          evidenceData.put(field.getName(),
            DateTimeTools.formatDateISO((Date) field.get(evidenceDetails)));

        } /*
           * else if (field.getType().isAssignableFrom(DateTime.class)) {
           *
           * evidenceData.put(field.getName(),
           * Long.toString(((DateTime) field.get(evidenceDetails)).asLong()));
           *
           * }
           */ else if (field.getType().isAssignableFrom(long.class)) {

          evidenceData.put(field.getName(),
            Long.toString((long) field.get(evidenceDetails)));

        } else {

          evidenceData.put(field.getName(),
            field.get(evidenceDetails).toString());
        }
      } catch (final IllegalAccessException e) {

        Trace.kTopLevelLogger.error(e.getMessage());
        final AppRuntimeException ex = new AppRuntimeException(e);
        throw ex;
      }

    }
    return evidenceData;
  }

  /**
   * This method assigns the evidence data to the evidence POJO and returns the
   * populated evidence data object.
   *
   * @param evidenceVO evidence POJO
   * @param details evidence data details
   * @return evidence data POJO
   */
  public Object setEvidenceData(final Object evidenceVO,
    final DynamicEvidenceDataDetails details) {

    try {

      for (final Field field : evidenceVO.getClass().getDeclaredFields()) {
        field.setAccessible(true);

        if (field.getName().equals("evidenceID")) {
          field.set(evidenceVO, details.getID());
          continue;
        }

        if (details.getAttribute(field.getName()) == null || StringUtil
          .isNullOrEmpty(details.getAttribute(field.getName()).getValue()))
          continue;

        if (field.getType().isAssignableFrom(Date.class)) {

          field.set(evidenceVO, DynamicEvidenceTypeConverter
            .convert(details.getAttribute(field.getName())));

        } else if (field.getType().isAssignableFrom(boolean.class)) {

          field.set(evidenceVO, Boolean
            .parseBoolean(details.getAttribute(field.getName()).getValue()));
        } else if (field.getType().isAssignableFrom(long.class)) {

          field.set(evidenceVO,
            Long.parseLong(details.getAttribute(field.getName()).getValue()));
        } else if (field.getType().isAssignableFrom(DateTime.class)) {

          field.set(evidenceVO, DynamicEvidenceTypeConverter
            .convert(details.getAttribute(field.getName())));
        } else {

          field.set(evidenceVO,
            details.getAttribute(field.getName()).getValue());
        }
      }

    } catch (final IllegalStateException | IllegalArgumentException
      | IllegalAccessException exc) {

      Trace.kTopLevelLogger.error(exc.getMessage());
      final AppRuntimeException exception = new AppRuntimeException(exc);
      throw exception;
    }

    return evidenceVO;
  }
}
