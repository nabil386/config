package curam.rules.functions;

import curam.datastore.impl.Datastore;
import curam.datastore.impl.DatastoreFactory;
import curam.datastore.impl.Entity;
import curam.datastore.impl.NoSuchSchemaException;
import curam.ieg.impl.IEG2Context;
import curam.ieg.impl.IEGScriptExecution;
import curam.ieg.impl.IEGScriptExecutionFactory;
import curam.intelligentevidencegathering.message.IEG;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.CustomFunctor;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Locale;
import static curam.rules.functions.CustomFunctionConstants.LABEL;
import static curam.rules.functions.CustomFunctionConstants.PERIOD_ENDDATE;
import static curam.rules.functions.CustomFunctionConstants.PERIOD_STARTDATE;

/**
 * Abstract class that can be extended by Custom Functions related to BDM
 * validations.
 *
 * @curam.exclude
 */
public abstract class BDMFunctor extends CustomFunctor {

  /**
   * Instantiates a new BDM functor.
   */
  public BDMFunctor() {

    super();
  }

  /**
   * Read root entity.
   *
   * @param rulesParameters the rules parameters
   * @return the entity
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  protected Entity readRoot(final RulesParameters rulesParameters)
    throws AppException, InformationalException {

    final Datastore ds = getDatastore(rulesParameters);

    return readRoot(rulesParameters, ds);
  }

  /**
   * Read root entity.
   *
   * @param rulesParameters the rules parameters
   * @param ds the ds
   * @return the entity
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  protected Entity readRoot(final RulesParameters rulesParameters,
    final Datastore ds) throws AppException, InformationalException {

    final IEG2Context ieg2Context = (IEG2Context) rulesParameters;
    final long rootEntityID = ieg2Context.getRootEntityID();

    return ds.readEntity(rootEntityID);
  }

  /**
   * Gets the datastore.
   *
   * @param rulesParameters the rules parameters
   * @return the datastore
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  protected Datastore getDatastore(final RulesParameters rulesParameters)
    throws AppException, InformationalException {

    final IEG2Context ieg2Context = (IEG2Context) rulesParameters;
    final long executionID = ieg2Context.getExecutionID();

    final IEGScriptExecution scriptExecution = IEGScriptExecutionFactory
      .getInstance().getScriptExecutionObject(executionID);
    Datastore ds = null;
    try {
      ds = DatastoreFactory.newInstance()
        .openDatastore(scriptExecution.getSchemaName());
    } catch (final NoSuchSchemaException e) {
      throw new AppException(IEG.ID_SCHEMA_NOT_FOUND);
    }
    return ds;
  }

  /**
   * Gets the locale.
   *
   * @return the locale
   */
  protected String getLocale() {

    return TransactionInfo.getProgramLocale();
  }

  /**
   * Formats a curam.util.type.Date with a given format.
   *
   * @param curamDate The date to format
   * @param format The format to use
   * @return formatted date
   */
  protected String formatDate(final Date curamDate, final String format) {

    final Locale local = new Locale(getLocale());
    final SimpleDateFormat sdf = new SimpleDateFormat(format, local);
    return sdf.format(curamDate.getCalendar().getTime());
  }

  /**
   * Parses a ISO Format Date to LocalDate
   *
   * @param isoDate the date in ISO Format
   * @return the local date
   */
  protected LocalDate parseLocalDate(final String isoDate) {

    final int year = Integer.parseInt(isoDate.substring(0, 4));
    final int month = Integer.parseInt(isoDate.substring(4, 6));
    final int day = Integer.parseInt(isoDate.substring(6));
    return LocalDate.of(year, month, day);
  }

  protected String getPeriodDisplayText(final Entity periodEntity) {

    final StringBuilder sb = new StringBuilder();
    sb.append("<li>");
    sb.append(periodEntity.getAttribute(PERIOD_STARTDATE + LABEL));
    sb.append(" - ");
    sb.append(periodEntity.getAttribute(PERIOD_ENDDATE + LABEL));
    sb.append("</li>");
    return sb.toString();
  }

  /**
   * Gets an optional string parameter.
   * If the parameter is not present it returns null.
   *
   * @param rulesParameters the rules parameters
   * @param index the index
   * @return the optional string parameter
   * @throws InformationalException the informational exception
   * @throws AppException the app exception
   */
  public String getOptionalStringParam(final RulesParameters rulesParameters,
    final int index) throws InformationalException, AppException {

    final StringAdaptor stringAdaptor =
      (StringAdaptor) getParameters().get(index);
    return stringAdaptor == null ? null
      : stringAdaptor.getStringValue(rulesParameters);
  }

  /**
   * Gets an optional Boolean parameter.
   * If the parameter is not present it returns null.
   *
   * @param rulesParameters the rules parameters
   * @param index the index
   * @return the optional Boolean parameter
   * @throws InformationalException the informational exception
   * @throws AppException the app exception
   */
  protected Boolean getOptionalBooleanParam(
    final RulesParameters rulesParameters, final int index)
    throws InformationalException, AppException {

    final BooleanAdaptor booleanAdapter =
      (BooleanAdaptor) getParameters().get(index);
    return booleanAdapter == null ? false
      : Boolean.parseBoolean(booleanAdapter.getStringValue(rulesParameters));
  }

  /**
   * Gets an optional Date parameter.
   * If the parameter is not present it returns null.
   *
   * @param rulesParameters the rules parameters
   * @param index the index
   * @return the optional Date parameter
   * @throws InformationalException the informational exception
   * @throws AppException the app exception
   */
  protected Date getOptionalDateParam(final RulesParameters rulesParameters,
    final int index) throws InformationalException, AppException {

    final DateAdaptor dateParameter =
      (DateAdaptor) getParameters().get(index);
    return dateParameter == null ? null
      : dateParameter.getValue(rulesParameters);
  }

  /**
   * Gets an optional money parameter.
   * If the parameter is not present it returns null.
   *
   * @param rulesParameters the rules parameters
   * @param index the index
   * @return the optional Money parameter
   * @throws InformationalException the informational exception
   * @throws AppException the app exception
   */
  public String getOptionalMoneyParam(final RulesParameters rulesParameters,
    final int index) throws InformationalException, AppException {

    final MoneyAdaptor moneyAdaptor =
      (MoneyAdaptor) getParameters().get(index);
    return moneyAdaptor == null ? null
      : moneyAdaptor.getStringValue(rulesParameters);
  }

  /**
   * Gets an optional long parameter.
   * If the parameter is not present it returns null.
   *
   * @param rulesParameters the rules parameters
   * @param index the index
   * @return the optional long parameter
   * @throws InformationalException the informational exception
   * @throws AppException the app exception
   */
  public String getOptionalLongParam(final RulesParameters rulesParameters,
    final int index) throws InformationalException, AppException {

    final LongAdaptor longAdaptor = (LongAdaptor) getParameters().get(index);
    return longAdaptor == null ? null
      : longAdaptor.getStringValue(rulesParameters);
  }

  /**
   * Gets an optional Double parameter.
   * If the parameter is not present it returns null.
   *
   * @param rulesParameters the rules parameters
   * @param index the index
   * @return the optional Double parameter
   * @throws InformationalException the informational exception
   * @throws AppException the app exception
   */
  public String getOptionalDoubleParam(final RulesParameters rulesParameters,
    final int index) throws InformationalException, AppException {

    final DoubleAdaptor doubleAdaptor =
      (DoubleAdaptor) getParameters().get(index);
    return doubleAdaptor == null ? null
      : doubleAdaptor.getStringValue(rulesParameters);
  }
}
