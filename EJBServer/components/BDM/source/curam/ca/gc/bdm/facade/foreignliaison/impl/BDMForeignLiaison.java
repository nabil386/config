package curam.ca.gc.bdm.facade.foreignliaison.impl;

import curam.ca.gc.bdm.sl.foreignliaison.fact.BDMMaintainForeignLiaisonFactory;
import curam.ca.gc.bdm.sl.foreignliaison.intf.BDMMaintainForeignLiaison;
import curam.ca.gc.bdm.sl.foreignliaison.struct.BDMFECaseIDKey;
import curam.ca.gc.bdm.sl.foreignliaison.struct.BDMFLAttachmentDetailsList;
import curam.ca.gc.bdm.sl.foreignliaison.struct.BDMForeignLiaisonDetails;
import curam.ca.gc.bdm.sl.foreignliaison.struct.BDMFrgnAppDetailsList;
import curam.ca.gc.bdm.sl.foreignliaison.struct.BDMFrgnLiasnAttLnkIDKey;
import curam.ca.gc.bdm.sl.foreignliaison.struct.BDMFrgnLiasnDeleteKey;
import curam.ca.gc.bdm.sl.foreignliaison.struct.BDMFrgnLiasnHistIDKey;
import curam.ca.gc.bdm.sl.foreignliaison.struct.BDMFrgnLiasnIDKey;
import curam.ca.gc.bdm.sl.foreignliaison.struct.BDMFrgnLiasnRefDetailsList;
import curam.ca.gc.bdm.sl.foreignliaison.struct.BDMFrgnLiasnViewDetails;
import curam.ca.gc.bdm.sl.foreignliaison.struct.BDMFrgnLiasnViewDetailsList;
import curam.ca.gc.bdm.sl.foreignliaison.struct.BDMFrgnLiasnWizardDetails;
import curam.ca.gc.bdm.sl.foreignliaison.struct.BDMLiasnChecklistCodeDetailsList;
import curam.ca.gc.bdm.sl.foreignliaison.struct.BDMReadDetailsKey;
import curam.ca.gc.bdm.sl.foreignliaison.struct.BDMReadDispDetails;
import curam.core.sl.struct.WizardStateID;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

public class BDMForeignLiaison
  extends curam.ca.gc.bdm.facade.foreignliaison.base.BDMForeignLiaison {

  /**
   * Method to list the foreign liaisons.
   *
   * @param BDMFECaseIDKey key
   * @return BDMFrgnLiasnViewDetailsList viewDetailsList.
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BDMFrgnLiasnViewDetailsList listForeignLiaisons(
    final BDMFECaseIDKey key) throws AppException, InformationalException {

    final BDMMaintainForeignLiaison maintainFLObj =
      BDMMaintainForeignLiaisonFactory.newInstance();

    final BDMFrgnLiasnViewDetailsList flViewDetList =
      maintainFLObj.listForeignLiaisons(key);

    return flViewDetList;
  }

  /**
   * Method to view the foreign liaison details.
   *
   * @param BDMFrgnLiasnIDKey key
   * @return BDMFrgnLiasnViewDetails viewDetails.
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BDMFrgnLiasnViewDetails viewForeignLiaisonDetails(
    final BDMFrgnLiasnIDKey key) throws AppException, InformationalException {

    final BDMMaintainForeignLiaison maintainFLObj =
      BDMMaintainForeignLiaisonFactory.newInstance();

    final BDMFrgnLiasnViewDetails flViewDetails =
      maintainFLObj.viewForeignLiaisonDetails(key);

    return flViewDetails;
  }

  /**
   * Method to view the foreign liaison history details.
   *
   * @param BDMFrgnLiasnHistIDKey key
   * @return BDMFrgnLiasnViewDetails viewHistoryDetails.
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BDMFrgnLiasnViewDetails
    viewForeignLiaisonHistoryDetails(final BDMFrgnLiasnHistIDKey key)
      throws AppException, InformationalException {

    final BDMMaintainForeignLiaison maintainFLObj =
      BDMMaintainForeignLiaisonFactory.newInstance();

    final BDMFrgnLiasnViewDetails flViewHistDetails =
      maintainFLObj.viewForeignLiaisonHistoryDetails(key);

    return flViewHistDetails;
  }

  /**
   * Method to list the foreign liaison's history.
   *
   * @param BDMFrgnLiasnIDKey key
   * @return BDMFrgnLiasnViewDetailsList detailsList.
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BDMFrgnLiasnViewDetailsList listFoerignLiaisonHistory(
    final BDMFrgnLiasnIDKey key) throws AppException, InformationalException {

    final BDMMaintainForeignLiaison maintainFLObj =
      BDMMaintainForeignLiaisonFactory.newInstance();

    final BDMFrgnLiasnViewDetailsList flViewHistDetList =
      maintainFLObj.listFoerignLiaisonHistory(key);

    return flViewHistDetList;
  }

  /**
   * Method to list the foreign liaison's attachments.
   *
   * @param BDMFrgnLiasnIDKey key
   * @return BDMFLAttachmentDetailsList detailsList.
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BDMFLAttachmentDetailsList listForeignLiaisonAttachments(
    final BDMFrgnLiasnIDKey key) throws AppException, InformationalException {

    final BDMMaintainForeignLiaison maintainFLObj =
      BDMMaintainForeignLiaisonFactory.newInstance();

    final BDMFLAttachmentDetailsList flAttDetailsList =
      maintainFLObj.listForeignLiaisonAttachments(key);

    return flAttDetailsList;
  }

  /**
   * Method to list the foreign liaison history attachments.
   *
   * @param BDMFrgnLiasnHistIDKey key
   * @return BDMFLAttachmentDetailsList detailsList.
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BDMFLAttachmentDetailsList
    listForeignLiaisonHistAtchmnts(final BDMFrgnLiasnHistIDKey key)
      throws AppException, InformationalException {

    final BDMMaintainForeignLiaison maintainFLObj =
      BDMMaintainForeignLiaisonFactory.newInstance();

    final BDMFLAttachmentDetailsList flHistAttDetailsList =
      maintainFLObj.listForeignLiaisonHistAtchmnts(key);

    return flHistAttDetailsList;
  }

  /**
   * Method to list the liaison checklist codes.
   *
   * @param
   * @return BDMLiasnChecklistCodeDetailsList wizardID.
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BDMLiasnChecklistCodeDetailsList listAllLiasnChecklistCodes()
    throws AppException, InformationalException {

    final BDMMaintainForeignLiaison maintainFLObj =
      BDMMaintainForeignLiaisonFactory.newInstance();

    final BDMLiasnChecklistCodeDetailsList lClCodeDetailsList =
      maintainFLObj.listAllLiasnChecklistCodes();

    return lClCodeDetailsList;
  }

  /**
   * Method to list the FECase attachments.
   *
   * @param BDMFECaseIDKey key
   * @return BDMFLAttachmentDetailsList detailsList.
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BDMFLAttachmentDetailsList listAttachmentsForFLByCaseID(
    final BDMFECaseIDKey key) throws AppException, InformationalException {

    final BDMMaintainForeignLiaison maintainFLObj =
      BDMMaintainForeignLiaisonFactory.newInstance();

    final BDMFLAttachmentDetailsList flAttForFLDetailsList =
      maintainFLObj.listAttachmentsForFLByCaseID(key);

    return flAttForFLDetailsList;
  }

  /**
   * Method to delete the foreign liaison.
   *
   * @param BDMFrgnLiasnIDKey key
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public void deleteForeignLiaison(final BDMFrgnLiasnDeleteKey key)
    throws AppException, InformationalException {

    final BDMMaintainForeignLiaison maintainFLObj =
      BDMMaintainForeignLiaisonFactory.newInstance();

    maintainFLObj.deleteForeignLiaison(key);
  }

  /**
   * Method to create the foreign liaison.
   *
   * @param BDMFrgnLiasnWizardDetails key
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public void
    createForeignLiaisonFromWizard(final BDMFrgnLiasnWizardDetails key)
      throws AppException, InformationalException {

    final BDMMaintainForeignLiaison maintainFLObj =
      BDMMaintainForeignLiaisonFactory.newInstance();

    maintainFLObj.createForeignLiaisonFromWizard(key);
  }

  /**
   * Method to list the foreign applications.
   *
   * @param BDMFECaseIDKey key
   * @return BDMFrgnAppDetailsList detailsList.
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BDMFrgnAppDetailsList listForeignAppsByCaseID(
    final BDMFECaseIDKey key) throws AppException, InformationalException {

    final BDMMaintainForeignLiaison maintainFLObj =
      BDMMaintainForeignLiaisonFactory.newInstance();

    final BDMFrgnAppDetailsList frgnAppDetalisList =
      maintainFLObj.listForeignAppsByCaseID(key);

    return frgnAppDetalisList;
  }

  /**
   * Method to list all the foreign liaisons of the foreign benefit.
   *
   */
  @Override
  public BDMFrgnLiasnRefDetailsList listLiaisonReferencesByCaseID(
    final BDMFECaseIDKey key) throws AppException, InformationalException {

    final BDMMaintainForeignLiaison maintainFLObj =
      BDMMaintainForeignLiaisonFactory.newInstance();

    final BDMFrgnLiasnRefDetailsList flRefDetailsList =
      maintainFLObj.listLiaisonReferencesByCaseID(key);

    return flRefDetailsList;
  }

  /**
   * Method to unlink the foreign liaison.
   *
   * @param BDMFrgnLiasnAttLnkIDKey key
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public void unlinkFrgnLiasnAttachment(final BDMFrgnLiasnAttLnkIDKey key)
    throws AppException, InformationalException {

    final BDMMaintainForeignLiaison maintainFLObj =
      BDMMaintainForeignLiaisonFactory.newInstance();

    maintainFLObj.unlinkFrgnLiasnAttachment(key);
  }

  /**
   * Method to read the foreign liaison details.
   *
   * @param BDMFrgnLiasnIDKey key
   * @return BDMForeignLiaisonDetails details..
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BDMForeignLiaisonDetails readForeignLiaisonDetails(
    final BDMFrgnLiasnIDKey key) throws AppException, InformationalException {

    final BDMMaintainForeignLiaison maintainFLObj =
      BDMMaintainForeignLiaisonFactory.newInstance();

    final BDMForeignLiaisonDetails flReadDetails =
      maintainFLObj.readForeignLiaisonDetails(key);

    return flReadDetails;
  }

  /**
   * Method to modify the foreign liaison details.
   *
   * @param BDMFrgnLiasnWizardDetails key
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public void
    modifyForeignLiaisonFromWizard(final BDMFrgnLiasnWizardDetails key)
      throws AppException, InformationalException {

    final BDMMaintainForeignLiaison maintainFLObj =
      BDMMaintainForeignLiaisonFactory.newInstance();

    maintainFLObj.modifyForeignLiaisonFromWizard(key);
  }

  /**
   * Method to read foreign liaison wizard details.
   *
   * @param WizardStateID key
   * @return BDMFrgnLiasnWizardDetails wizDetails.
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BDMFrgnLiasnWizardDetails readForeignLiaisonWizardDetails(
    final WizardStateID key) throws AppException, InformationalException {

    final BDMMaintainForeignLiaison maintainFLObj =
      BDMMaintainForeignLiaisonFactory.newInstance();

    final BDMFrgnLiasnWizardDetails frgnLiasnWizardDetails =
      maintainFLObj.readForeignLiaisonWizardDetails(key);
    return frgnLiasnWizardDetails;
  }

  /**
   * Method to create and populate foreign liaison wizard details.
   *
   * @param BDMFrgnLiasnIDKey key
   * @return WizardStateID wizardID.
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public WizardStateID createAndPopulateFrgnLiasnWizard(
    final BDMFrgnLiasnIDKey key) throws AppException, InformationalException {

    final BDMMaintainForeignLiaison maintainFLObj =
      BDMMaintainForeignLiaisonFactory.newInstance();

    final WizardStateID wizStateID =
      maintainFLObj.createAndPopulateFrgnLiasnWizard(key);

    return wizStateID;
  }

  /**
   * Method to create the foreign liaison checklist.
   *
   * @param BDMFrgnLiasnWizardDetails key
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public void createFLChecklistFromWizard(final BDMFrgnLiasnWizardDetails key)
    throws AppException, InformationalException {

    final BDMMaintainForeignLiaison maintainFLObj =
      BDMMaintainForeignLiaisonFactory.newInstance();

    maintainFLObj.createFLChecklistFromWizard(key);
  }

  /**
   * Method to create the foreign liaison attachments.
   *
   * @param BDMFrgnLiasnWizardDetails key
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public void
    createFLAttachmentsFromWizard(final BDMFrgnLiasnWizardDetails key)
      throws AppException, InformationalException {

    final BDMMaintainForeignLiaison maintainFLObj =
      BDMMaintainForeignLiaisonFactory.newInstance();

    maintainFLObj.createFLAttachmentsFromWizard(key);
  }

  /**
   * Method to modify the foreign liaison checklist details.
   *
   * @param BDMFrgnLiasnWizardDetails key
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public void modifyFLChecklistFromWizard(final BDMFrgnLiasnWizardDetails key)
    throws AppException, InformationalException {

    final BDMMaintainForeignLiaison maintainFLObj =
      BDMMaintainForeignLiaisonFactory.newInstance();

    maintainFLObj.modifyFLChecklistFromWizard(key);
  }

  /**
   * Method to modify the foreign liaison attachments details.
   *
   * @param BDMFrgnLiasnWizardDetails key
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public void
    modifyFLAttachmentsFromWizard(final BDMFrgnLiasnWizardDetails key)
      throws AppException, InformationalException {

    final BDMMaintainForeignLiaison maintainFLObj =
      BDMMaintainForeignLiaisonFactory.newInstance();

    maintainFLObj.modifyFLAttachmentsFromWizard(key);
  }

  /**
   * Method to read details for creating/modifying liaison.
   *
   * @param WizardStateID key
   * @return BDMFrgnLiasnWizardDetails wizDetails.
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BDMReadDispDetails readDetailsForForeignLiaison(
    final BDMReadDetailsKey key) throws AppException, InformationalException {

    final BDMMaintainForeignLiaison maintainFLObj =
      BDMMaintainForeignLiaisonFactory.newInstance();

    final BDMReadDispDetails readDetails =
      maintainFLObj.readDetailsForForeignLiaison(key);

    return readDetails;
  }
}
