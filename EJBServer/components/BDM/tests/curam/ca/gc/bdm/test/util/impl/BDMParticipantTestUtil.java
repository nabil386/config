package curam.ca.gc.bdm.test.util.impl;

import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.DateTime;

public class BDMParticipantTestUtil {

  /**
   * Get current date time string to append in to the names, so that participant
   * registration will always have unique name.
   *
   * @return date time string.
   */
  public static String getCurrentDateTimeString()
    throws AppException, InformationalException {

    final String dateTimeStr =
      DateTime.getCurrentDateTime().toString().replaceAll(":", "")
        .replaceAll("-", "").replaceAll("/", "").replaceAll(" ", "");

    return dateTimeStr;
  }

  /**
   * Method to build the unique name for participant registration.
   *
   * @param partcipantNamePrefix
   * @return unique participant name.
   * @throws AppException
   * @throws InformationalException
   */
  public static String
    getUniqueNameForParticipant(final String partcipantNamePrefix)
      throws AppException, InformationalException {

    String uniquePtcptName = partcipantNamePrefix;
    final String dateTimeStr = getCurrentDateTimeString();

    uniquePtcptName = uniquePtcptName + dateTimeStr;

    return uniquePtcptName;
  }

}
