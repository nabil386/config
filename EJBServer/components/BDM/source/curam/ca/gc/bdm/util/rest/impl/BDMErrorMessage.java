package curam.ca.gc.bdm.util.rest.impl;

/**
 * Helper class to construct the Error response scenarios. This is similar to
 * OOTB REST response that happens when there are error scenarios.
 */
public class BDMErrorMessage {

  int code;

  String message;

  String level;

  String message_id;

  BDMErrorMessage() {

  }

  public BDMErrorMessage(final int code, final String message,
    final String level, final String message_id) {

    this.code = code;
    this.message = message;
    this.message_id = message_id;
    this.level = level;
  }

  public void setCode(final int code) {

    this.code = code;
  }

  public void setMessage(final String message) {

    this.message = message;
  }

  public void setLevel(final String level) {

    this.level = level;
  }

  public void setmessage_id(final String message_id) {

    this.level = message_id;
  }

  public int getCode() {

    return this.code;
  }

  public String getMessage() {

    return this.message;
  }

  public String getLevel() {

    return this.level;
  }

  public String getMessage_id() {

    return this.message_id;
  }
}
