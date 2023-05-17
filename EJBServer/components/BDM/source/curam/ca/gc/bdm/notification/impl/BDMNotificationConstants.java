package curam.ca.gc.bdm.notification.impl;

import curam.core.impl.EnvVars;
import curam.util.resources.Configuration;

public class BDMNotificationConstants {

  public static final String kSuspendBenefitNotiticationBenefitName =
    "benefit.product.type.code";

  public static final String kSuspendBenefitNotiticationDate =
    "suspension.date";

  public static final String kCorrespondenceUnreadMessageBodySingular =
    "Correspondence.UnreadMail.Body.Singular";

  public static final String kCorrespondenceUnreadMessageBodyPlural =
    "Correspondence.UnreadMail.Body.Plural";

  public static final String kCorrespondenceUnreadMessageTitleSingular =
    "Correspondence.UnreadMail.Title.Plural";

  public static final String kCorrespondenceUnreadMessageTitlePlural =
    "Correspondence.UnreadMail.Title.Plural";

  public static final String kCorrespondenceUnreadMessageLink =
    "Correspondence.List.Link";

  public static final String kCorrespondenceUnreadMessageTarget =
    "Correspondence.List.Target";

  public static final String kBDMNotificationPropertyFile = Configuration
    .getProperty(EnvVars.BDM_ENV_NOTIFICATION_FILENAME_CLAIMDENIED);

  public static final String kGCNotifyRealTimeTag = "@GC_NOTIFY_REAL_TIME";

  public static final String kCallBackRequestTitle =
    "BDM.Notification.CallBackRequest.Title";

  public static final String kCallBackRequestMessageBody =
    "BDM.Notification.CallBackRequest.Body";

  public static final String kCallBackRequestDate = "callbackrequest.date";

  public static final String kCallBackRequestTime = "callbackrequest.time";

}
