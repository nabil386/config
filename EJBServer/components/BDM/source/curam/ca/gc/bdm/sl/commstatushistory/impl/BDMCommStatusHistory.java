package curam.ca.gc.bdm.sl.commstatushistory.impl;

import com.google.inject.ImplementedBy;
import curam.util.persistence.StandardEntity;
import curam.util.type.DateTime;

@ImplementedBy(BDMCommStatusHistoryImpl.class)
public interface BDMCommStatusHistory extends StandardEntity {

  /**
   * Returns the communication status history identifier.
   *
   * @return the communication status history identifier.
   */
  @Override
  public Long getID();

  /**
   * Returns the communication status history identifie.
   *
   * @return the communication status history identifie.
   */
  public long getCommunicationID();

  /**
   * Returns the status code.
   *
   * @return code table entry value for the status.
   */
  public String getStatusCode();

  /**
   * Returns the status codetable Name.
   *
   * @return code table name value for the status code captured.
   */
  public String getStatusCTTableName();

  /**
   * Returns the datetime value of status.
   *
   * @return datetime value for status code captured.
   */
  public DateTime getStatusDataTime();

  /**
   * Returns the userName made status change.
   *
   * @return the userName.
   */
  public String getUserName();
}
