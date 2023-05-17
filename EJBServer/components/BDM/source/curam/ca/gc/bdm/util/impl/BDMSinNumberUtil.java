package curam.ca.gc.bdm.util.impl;

import curam.ca.gc.bdm.facade.person.struct.BDMPersonSearchDetailsResult;
import curam.core.impl.EnvVars;
import curam.util.resources.Configuration;
import curam.util.type.Date;
import java.util.Objects;
import java.util.stream.Collectors;
import static java.util.stream.IntStream.range;

/*
 * Task 61712 mask sin number on person search and person registration pages
 * by nhamade on, 11 Jul 2022
 */
public class BDMSinNumberUtil {

  private static final int SIN_NUMBER_CHAR_DISPLAYED = 4;

  public static void maskSinNumber(
    final BDMPersonSearchDetailsResult bdmPersonSearchDetailsResult) {

    range(0, bdmPersonSearchDetailsResult.personSearchResult.dtlsList.size())
      .filter(i -> bdmPersonSearchDetailsResult.personSearchResult.dtlsList
        .get(i).sinNumber != null
        && !bdmPersonSearchDetailsResult.personSearchResult.dtlsList
          .get(i).sinNumber.isEmpty()
        && bdmPersonSearchDetailsResult.personSearchResult.dtlsList
          .get(i).sinNumber.length() > SIN_NUMBER_CHAR_DISPLAYED)
      .forEach(i -> {
        final String sinNumber =
          bdmPersonSearchDetailsResult.personSearchResult.dtlsList
            .get(i).sinNumber;
        bdmPersonSearchDetailsResult.personSearchResult.dtlsList
          .get(i).sinNumber = replaceSinNumberWithAsterisks(sinNumber);
        if (bdmPersonSearchDetailsResult.personSearchResult.dtlsList
          .get(i).xmlPersonData != null
          && bdmPersonSearchDetailsResult.personSearchResult.dtlsList
            .get(i).xmlPersonData.contains(sinNumber)) {
          final String xmlPersonData =
            bdmPersonSearchDetailsResult.personSearchResult.dtlsList
              .get(i).xmlPersonData.replace(sinNumber,
                replaceSinNumberWithAsterisks(sinNumber));
          bdmPersonSearchDetailsResult.personSearchResult.dtlsList
            .get(i).xmlPersonData = xmlPersonData;
        }
      });
  }

  public static String replaceSinNumberWithAsterisks(final String sin) {

    final int sinLength = sin.length();
    final String lastFourDigits =
      sin.substring(sinLength - SIN_NUMBER_CHAR_DISPLAYED, sinLength);
    return range(0, sinLength - SIN_NUMBER_CHAR_DISPLAYED).mapToObj(i -> "*")
      .collect(Collectors.joining("", "", lastFourDigits));
  }

  public static boolean isValidSearchCombination(final String refNumber,
    final String trackingNumber, final String firstName,
    final String lastName, final Date dateOfBirth, final String state,
    final String postalCode, final String country,
    final String birthLastName) {

    if (!refNumber.isEmpty() || !trackingNumber.isEmpty()) {
      return true;
    } else if (Objects.nonNull(firstName) && !firstName.isEmpty()
      && Objects.nonNull(lastName) && !lastName.isEmpty()
      && (!state.isEmpty() || !postalCode.isEmpty() || !country.isEmpty()
        || !dateOfBirth.equals(Date.kZeroDate))) {
      return true;
    } else if (Objects.nonNull(firstName) && !firstName.isEmpty()
      && Objects.nonNull(birthLastName) && !birthLastName.isEmpty()
      && (!state.isEmpty() || !postalCode.isEmpty() || !country.isEmpty()
        || !dateOfBirth.equals(Date.kZeroDate))) {
      return true;
    }
    return false;
  }

  public static boolean isValidSearchCriteria(final String refNumber,
    final String trackingNumber, final String firstName,
    final String lastName, final String birthLastName) {

    if ((Objects.isNull(refNumber) || refNumber.isEmpty())
      && (Objects.isNull(trackingNumber) || trackingNumber.isEmpty())) {
      final int limit = Integer.valueOf(
        Configuration.getProperty(EnvVars.BDM_PERSON_SEARCH_NAME_LIMIT));
      if (Objects.nonNull(firstName) && firstName.length() >= limit
        && Objects.nonNull(lastName) && lastName.length() >= limit) {
        return true;
      }
      if (Objects.nonNull(firstName) && firstName.length() >= limit
        && Objects.nonNull(birthLastName)
        && birthLastName.length() >= limit) {
        return true;
      }
      if ((Objects.isNull(refNumber) || refNumber.isEmpty())
        && (Objects.isNull(trackingNumber) || trackingNumber.isEmpty())
        && (Objects.isNull(firstName) || firstName.isEmpty())
        && (Objects.isNull(lastName) || lastName.isEmpty())) {
        return true;
      }
      return false;
    }
    return true;
  }

}
