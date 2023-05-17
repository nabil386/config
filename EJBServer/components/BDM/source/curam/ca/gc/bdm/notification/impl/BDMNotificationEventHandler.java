package curam.ca.gc.bdm.notification.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.events.BDMNOTIFICATION;
import curam.util.events.impl.EventFilter;
import curam.util.events.impl.EventHandler;
import curam.util.events.struct.Event;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;

public class BDMNotificationEventHandler
  implements EventHandler, EventFilter {

  @Inject
  private BDMNotification bdmNotification;

  public BDMNotificationEventHandler() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Event handler for accepting the raised event
   *
   * @return boolean
   * @param Event Object
   */
  @Override
  public boolean accept(final Event event)
    throws AppException, InformationalException {

    // check if the event is the BDMNotification event class
    if (event.eventKey.eventClass
      .equals(BDMNOTIFICATION.CREATECLAIMDENIEDNOTIFICATION.eventClass)
      || event.eventKey.eventClass
        .equals(BDMNOTIFICATION.CREATECLAIMESTABLISHEDNOTIFICATION.eventClass)
      || event.eventKey.eventClass
        .equals(BDMNOTIFICATION.CREATESUSPENDBENEFITCASE.eventClass))
      return true;

    return false;
  }

  /**
   * Event handler for raising the event and call the BDMNotification for
   * creating the Claim established and denied notification
   *
   * @return boolean
   * @param Event Object
   */
  @Override
  public void eventRaised(final Event event)
    throws AppException, InformationalException {

    // if event type is CREATECLAIMDENIEDNOTIFICATION calls the BDM Notification
    // createClaimDEniedNotificationMessage
    if (event.eventKey.eventClass
      .equals(BDMNOTIFICATION.CREATECLAIMDENIEDNOTIFICATION.eventClass)
      && event.eventKey.eventType
        .equals(BDMNOTIFICATION.CREATECLAIMDENIEDNOTIFICATION.eventType)) {
      bdmNotification.createClaimDeniedNotificationMessage(
        event.primaryEventData, event.secondaryEventData);
    }
    // else if event is CREATECLAIMESTABLISHEDNOTIFICATION calls the BDM
    // Notification createClaimEstablishedNotificationMessage
    else if (event.eventKey.eventClass
      .equals(BDMNOTIFICATION.CREATECLAIMESTABLISHEDNOTIFICATION.eventClass)
      && event.eventKey.eventType.equals(
        BDMNOTIFICATION.CREATECLAIMESTABLISHEDNOTIFICATION.eventType)) {
      bdmNotification.createClaimEstablishedNotificationMessage(
        event.primaryEventData, event.secondaryEventData);
    }

    else if (event.eventKey.eventClass
      .equals(BDMNOTIFICATION.CREATESUSPENDBENEFITCASE.eventClass)
      && event.eventKey.eventType
        .equals(BDMNOTIFICATION.CREATESUSPENDBENEFITCASE.eventType)) {
      bdmNotification.createSuspendBenefitCaseNotificationMessage(
        event.primaryEventData, event.secondaryEventData);
    } else if (event.eventKey.eventClass
      .equals(BDMNOTIFICATION.CREATEUNSUSPENDBENEFITCASE.eventClass)
      && event.eventKey.eventType
        .equals(BDMNOTIFICATION.CREATEUNSUSPENDBENEFITCASE.eventType)) {
      bdmNotification.triggerUnsuspendBenefitCaseNotificationMessage(
        event.primaryEventData, event.secondaryEventData);
    }
  }
}
