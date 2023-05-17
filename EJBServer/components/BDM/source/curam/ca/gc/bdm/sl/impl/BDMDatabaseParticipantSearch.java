/**
 *
 */
package curam.ca.gc.bdm.sl.impl;

import curam.codetable.CONCERNROLETYPE;
import curam.core.sl.struct.AllParticipantSearchKey;
import curam.core.sl.struct.AllParticipantSearchResult;
import curam.core.sl.struct.AllParticipantSearchStruct;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

/**
 * Added customized version of all participant search to
 * support third party contact search.
 *
 * @author donghua.jin
 *
 */
public class BDMDatabaseParticipantSearch
  extends curam.ca.gc.bdm.sl.base.BDMDatabaseParticipantSearch {

  /**
   * Do search only on concern role type of Person, Prospect Person
   * and external party.
   *
   * @param key
   */
  @Override
  public AllParticipantSearchResult
    searchAll(final AllParticipantSearchKey key)
      throws AppException, InformationalException {

    final AllParticipantSearchResult participantSearchResult =
      new AllParticipantSearchResult();

    final AllParticipantSearchStruct allParticipantSearchStruct =
      new AllParticipantSearchStruct();

    allParticipantSearchStruct.assign(key);

    participantSearchResult.dtlsList
      .addAll(searchByType(allParticipantSearchStruct,
        formatPersonSQL(allParticipantSearchStruct)).dtlsList);

    allParticipantSearchStruct.concernRoleType =
      CONCERNROLETYPE.EXTERNALPARTY;
    participantSearchResult.dtlsList
      .addAll(searchByType(allParticipantSearchStruct,
        formatSQLByType(allParticipantSearchStruct)).dtlsList);

    return participantSearchResult;
  }

}
