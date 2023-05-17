package curam.ca.gc.bdmoas.impl;

import curam.ca.gc.bdmoas.facade.cra.transaction.struct.BDMOASCRARequestTaxYear;
import curam.ca.gc.bdmoas.facade.cra.transaction.struct.BDMOASCRARequestTaxYearList;
import java.util.Calendar;

public class BDMOASUtil {

  public static BDMOASCRARequestTaxYearList GetFinacilatxYearList() {

    // Tax Year Implementation
    final BDMOASCRARequestTaxYearList list =
      new BDMOASCRARequestTaxYearList();
    BDMOASCRARequestTaxYear taxYear;
    final Calendar currentCal = Calendar.getInstance();
    final curam.util.type.Date currentDate =
      curam.util.type.Date.getFromJavaUtilDate(currentCal.getTime());
    final int currYear = currentCal.get(Calendar.YEAR);
    final curam.util.type.Date statFinYrDate =
      curam.util.type.Date.fromISO8601(currYear + "0101");
    final curam.util.type.Date midFinYrDate =
      curam.util.type.Date.fromISO8601(currYear + "0630");
    final curam.util.type.Date endFinYrDate =
      curam.util.type.Date.fromISO8601(currYear + "1231");
    if ((currentDate.after(statFinYrDate)
      || currentDate.equals(statFinYrDate))
      && (currentDate.before(midFinYrDate)
        || currentDate.equals(midFinYrDate))) {
      taxYear = new BDMOASCRARequestTaxYear();
      taxYear.taxYear = Integer.toString(currYear - 1);
      list.taxyears.add(taxYear);
      taxYear = new BDMOASCRARequestTaxYear();
      taxYear.taxYear = Integer.toString(currYear - 2);
      list.taxyears.add(taxYear);
      taxYear = new BDMOASCRARequestTaxYear();
      taxYear.taxYear = Integer.toString(currYear - 3);
      list.taxyears.add(taxYear);
      taxYear = new BDMOASCRARequestTaxYear();
      taxYear.taxYear = Integer.toString(currYear - 4);
      list.taxyears.add(taxYear);
      taxYear = new BDMOASCRARequestTaxYear();
      taxYear.taxYear = Integer.toString(currYear - 5);
      list.taxyears.add(taxYear);

    }
    if (currentDate.after(midFinYrDate) && currentDate.before(endFinYrDate)
      || currentDate.equals(endFinYrDate)) {
      taxYear = new BDMOASCRARequestTaxYear();
      taxYear.taxYear = Integer.toString(currYear - 1);
      list.taxyears.add(taxYear);
      taxYear = new BDMOASCRARequestTaxYear();
      taxYear.taxYear = Integer.toString(currYear - 2);
      list.taxyears.add(taxYear);
      taxYear = new BDMOASCRARequestTaxYear();
      taxYear.taxYear = Integer.toString(currYear - 3);
      list.taxyears.add(taxYear);
      taxYear = new BDMOASCRARequestTaxYear();
      taxYear.taxYear = Integer.toString(currYear - 4);
      list.taxyears.add(taxYear);
    }
    return list;
  }
}
