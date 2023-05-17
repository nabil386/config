package curam.ca.gc.bdm.test.interfaces.sl.gcnotify.impl;

import com.google.gson.Gson;
import curam.ca.gc.bdm.codetable.BDMGCNOTIFYALERTTYPE;
import curam.ca.gc.bdm.codetable.BDMGCNotifyTemplateType;
import curam.ca.gc.bdm.codetable.BDMLANGUAGE;
import curam.ca.gc.bdm.entity.bdmgcnotify.struct.BDMGcNotifyRequestDataDetails;
import curam.ca.gc.bdm.entity.bdmgcnotify.struct.BDMGcNotifyRequestDataDetailsList;
import curam.ca.gc.bdm.sl.interfaces.gcnotify.impl.BDMGCNotifyInterfaceImpl;
import curam.ca.gc.bdm.sl.interfaces.gcnotify.impl.gcnotifypojos.BDMGCNotifyResponse;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.core.impl.EnvVars;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import java.io.IOException;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BDMGCNotifyBulkSMSTest extends CuramServerTestJUnit4 {

  /**
   * Instantiates a new BDMGCNotify test.
   */
  public BDMGCNotifyBulkSMSTest() {

    super();

  }

  BDMGcNotifyRequestDataDetailsList dtlsList;

  BDMGcNotifyRequestDataDetails dtls1;

  BDMGcNotifyRequestDataDetails dtls2;

  BDMGCNotifyInterfaceImpl testClass;

  static String BDM_GCNOTIFY_FRENCH_NOTIFICATION_SMS_TEMPLATE_ID =
    Configuration
      .getProperty(EnvVars.BDM_GCNOTIFY_FRENCH_NOTIFICATION_SMS_TEMPLATE_ID);

  static String BDM_GCNOTIFY_ENGLISH_NOTIFICATION_SMS_TEMPLATE_ID =
    Configuration
      .getProperty(EnvVars.BDM_GCNOTIFY_ENGLISH_NOTIFICATION_SMS_TEMPLATE_ID);

  @Ignore
  @Test
  public void bulkSMStestFrenchNotification()
    throws AppException, InformationalException, IOException {

    // all records in the list must have unique templateID
    dtlsList = new BDMGcNotifyRequestDataDetailsList();
    dtls1 = new BDMGcNotifyRequestDataDetails();
    dtls2 = new BDMGcNotifyRequestDataDetails();
    testClass = new BDMGCNotifyInterfaceImpl();

    dtls1.recordID = 11;
    dtls1.fullName = "xxxx xxxx";
    dtls1.phoneNumber = "11111111";
    dtls1.alertType = BDMGCNOTIFYALERTTYPE.BDM_GCNOTIFY_PHONE;
    dtls1.language = BDMLANGUAGE.FRENCHL;
    dtls1.templateType = BDMGCNotifyTemplateType.BDM_GC_NF;
    dtls1.templateID = BDM_GCNOTIFY_FRENCH_NOTIFICATION_SMS_TEMPLATE_ID;
    dtlsList.dtls.add(dtls1);

    dtls2.recordID = 12;
    dtls2.fullName = "xxxx xxxx";
    dtls2.phoneNumber = "306111111";
    dtls1.alertType = BDMGCNOTIFYALERTTYPE.BDM_GCNOTIFY_PHONE;
    dtls1.language = BDMLANGUAGE.FRENCHL;
    dtls1.templateType = BDMGCNotifyTemplateType.BDM_GC_NF;
    dtls1.templateID = BDM_GCNOTIFY_FRENCH_NOTIFICATION_SMS_TEMPLATE_ID;
    dtlsList.dtls.add(dtls2);

    final String gcNotifySmsResponseJson =
      testClass.sendGCNotifyBulkSMSRequest(dtlsList);
    final Gson gson = new Gson();
    final BDMGCNotifyResponse responseObj =
      gson.fromJson(gcNotifySmsResponseJson, BDMGCNotifyResponse.class);
    final int smsResponseCode = responseObj.getResponseCode();
    final String emailResponse = responseObj.getResponse();
    // These are non-registered test data in GCNotify account, hence we expect
    // to fail, for registered user it must pass
    assertEquals("Bad Request", emailResponse);
    assertEquals(400, smsResponseCode);
  }

  @Ignore
  @Test
  public void bulkSMStestFrenchCorrespondance()
    throws AppException, InformationalException, IOException {

    // all records in the list must have unique templateID
    dtlsList = new BDMGcNotifyRequestDataDetailsList();
    dtls1 = new BDMGcNotifyRequestDataDetails();
    dtls2 = new BDMGcNotifyRequestDataDetails();
    testClass = new BDMGCNotifyInterfaceImpl();

    dtls1.recordID = 11;
    dtls1.fullName = "xxxx xxxx";
    dtls1.phoneNumber = "11111111";
    dtls1.alertType = BDMGCNOTIFYALERTTYPE.BDM_GCNOTIFY_PHONE;
    dtls1.language = BDMLANGUAGE.FRENCHL;
    dtls1.templateType = BDMGCNotifyTemplateType.BDM_GC_COR;
    dtls1.templateID = BDM_GCNOTIFY_FRENCH_NOTIFICATION_SMS_TEMPLATE_ID;
    dtlsList.dtls.add(dtls1);

    dtls2.recordID = 12;
    dtls2.fullName = "xxxx xxxx";
    dtls2.phoneNumber = "306111111";
    dtls1.alertType = BDMGCNOTIFYALERTTYPE.BDM_GCNOTIFY_PHONE;
    dtls1.language = BDMLANGUAGE.FRENCHL;
    dtls1.templateType = BDMGCNotifyTemplateType.BDM_GC_COR;
    dtls1.templateID = BDM_GCNOTIFY_FRENCH_NOTIFICATION_SMS_TEMPLATE_ID;
    dtlsList.dtls.add(dtls2);

    final String gcNotifySmsResponseJson =
      testClass.sendGCNotifyBulkSMSRequest(dtlsList);
    final Gson gson = new Gson();
    final BDMGCNotifyResponse responseObj =
      gson.fromJson(gcNotifySmsResponseJson, BDMGCNotifyResponse.class);
    final int smsResponseCode = responseObj.getResponseCode();
    final String emailResponse = responseObj.getResponse();
    // These are non-registered test data in GCNotify account, hence we expect
    // to fail, for registered user it must pass
    assertEquals("Bad Request", emailResponse);
    assertEquals(400, smsResponseCode);
  }

  @Ignore
  @Test
  public void bulkSMStestEnglishNotification()
    throws AppException, InformationalException, IOException {

    // all records in the list must have unique templateID
    dtlsList = new BDMGcNotifyRequestDataDetailsList();
    dtls1 = new BDMGcNotifyRequestDataDetails();
    dtls2 = new BDMGcNotifyRequestDataDetails();
    testClass = new BDMGCNotifyInterfaceImpl();

    dtls1.recordID = 11;
    dtls1.fullName = "xxxx xxxx";
    dtls1.phoneNumber = "11111111";
    dtls1.alertType = BDMGCNOTIFYALERTTYPE.BDM_GCNOTIFY_PHONE;
    dtls1.language = BDMLANGUAGE.ENGLISHL;
    dtls1.templateType = BDMGCNotifyTemplateType.BDM_GC_NF;
    dtls1.templateID = BDM_GCNOTIFY_ENGLISH_NOTIFICATION_SMS_TEMPLATE_ID;
    dtlsList.dtls.add(dtls1);

    dtls2.recordID = 12;
    dtls2.fullName = "xxxx xxxx";
    dtls2.phoneNumber = "306111111";
    dtls1.alertType = BDMGCNOTIFYALERTTYPE.BDM_GCNOTIFY_PHONE;
    dtls1.language = BDMLANGUAGE.ENGLISHL;
    dtls1.templateType = BDMGCNotifyTemplateType.BDM_GC_NF;
    dtls1.templateID = BDM_GCNOTIFY_ENGLISH_NOTIFICATION_SMS_TEMPLATE_ID;
    dtlsList.dtls.add(dtls2);

    final String gcNotifySmsResponseJson =
      testClass.sendGCNotifyBulkSMSRequest(dtlsList);
    final Gson gson = new Gson();
    final BDMGCNotifyResponse responseObj =
      gson.fromJson(gcNotifySmsResponseJson, BDMGCNotifyResponse.class);
    final int smsResponseCode = responseObj.getResponseCode();
    final String emailResponse = responseObj.getResponse();
    // These are non-registered test data in GCNotify account, hence we expect
    // to fail, for registered user it must pass
    assertEquals("Bad Request", emailResponse);
    assertEquals(400, smsResponseCode);
  }

  @Ignore
  @Test
  public void bulkSMStestEnglishCorrespondance()
    throws AppException, InformationalException, IOException {

    // all records in the list must have unique templateID
    dtlsList = new BDMGcNotifyRequestDataDetailsList();
    dtls1 = new BDMGcNotifyRequestDataDetails();
    dtls2 = new BDMGcNotifyRequestDataDetails();
    testClass = new BDMGCNotifyInterfaceImpl();

    dtls1.recordID = 11;
    dtls1.fullName = "xxxx xxxx";
    dtls1.phoneNumber = "11111111";
    dtls1.alertType = BDMGCNOTIFYALERTTYPE.BDM_GCNOTIFY_PHONE;
    dtls1.language = BDMLANGUAGE.ENGLISHL;
    dtls1.templateType = BDMGCNotifyTemplateType.BDM_GC_COR;
    dtls1.templateID = BDM_GCNOTIFY_ENGLISH_NOTIFICATION_SMS_TEMPLATE_ID;
    dtlsList.dtls.add(dtls1);

    dtls2.recordID = 12;
    dtls2.fullName = "xxxx xxxx";
    dtls2.phoneNumber = "306111111";
    dtls1.alertType = BDMGCNOTIFYALERTTYPE.BDM_GCNOTIFY_PHONE;
    dtls1.language = BDMLANGUAGE.ENGLISHL;
    dtls1.templateType = BDMGCNotifyTemplateType.BDM_GC_COR;
    dtls1.templateID = BDM_GCNOTIFY_ENGLISH_NOTIFICATION_SMS_TEMPLATE_ID;
    dtlsList.dtls.add(dtls2);

    final String gcNotifySmsResponseJson =
      testClass.sendGCNotifyBulkSMSRequest(dtlsList);
    final Gson gson = new Gson();
    final BDMGCNotifyResponse responseObj =
      gson.fromJson(gcNotifySmsResponseJson, BDMGCNotifyResponse.class);
    final int smsResponseCode = responseObj.getResponseCode();
    final String emailResponse = responseObj.getResponse();
    // These are non-registered test data in GCNotify account, hence we expect
    // to fail, for registered user it must pass
    assertEquals("Bad Request", emailResponse);
    assertEquals(400, smsResponseCode);
  }

}
