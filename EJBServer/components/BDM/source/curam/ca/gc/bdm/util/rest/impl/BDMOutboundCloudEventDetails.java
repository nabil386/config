package curam.ca.gc.bdm.util.rest.impl;

/**
 * Helper class to construct the cloud event details for the outbounds. This
 * class will have the attributes and getter/setter methods to construct the
 * cloudevent.
 */
public class BDMOutboundCloudEventDetails {

  private final String specversion;

  private final String id;

  private final String source;

  private final String type;

  private final String datacontenttype;

  private final String dataschema;

  private final String subject;

  private final String time;

  private final String partitionkey;

  private final String filesequencenumber;

  private final String sequence;

  private final String totalcount;

  public String getId() {

    return id;
  }

  public String getSpecVersion() {

    return specversion;
  }

  public String getSource() {

    return source;
  }

  public String getType() {

    return type;
  }

  public String getDatacontenttype() {

    return datacontenttype;
  }

  public String getDataschema() {

    return dataschema;
  }

  public String getSubject() {

    return subject;
  }

  public String getTime() {

    return time;
  }

  public String getPartitionkey() {

    return partitionkey;
  }

  public String getSequence() {

    return sequence;
  }

  public String getFilesequencenumber() {

    return filesequencenumber;
  }

  public String getTotalcount() {

    return totalcount;
  }

  @SuppressWarnings("synthetic-access")
  private BDMOutboundCloudEventDetails(
    final BDMOutboundCloudEventDetailsBuilder cloudEventDetailsBuilder) {

    this.specversion = cloudEventDetailsBuilder.specversion;
    this.id = cloudEventDetailsBuilder.id;
    this.source = cloudEventDetailsBuilder.source;
    this.type = cloudEventDetailsBuilder.type;
    this.datacontenttype = cloudEventDetailsBuilder.datacontenttype;
    this.dataschema = cloudEventDetailsBuilder.dataschema;
    this.subject = cloudEventDetailsBuilder.subject;
    this.time = cloudEventDetailsBuilder.time;
    this.partitionkey = cloudEventDetailsBuilder.partitionkey;
    this.filesequencenumber = cloudEventDetailsBuilder.filesequencenumber;
    this.sequence = cloudEventDetailsBuilder.sequence;
    this.totalcount = cloudEventDetailsBuilder.totalcount;
  }

  public static class BDMOutboundCloudEventDetailsBuilder {

    private String specversion;

    private String id;

    private String source;

    private String type;

    private String datacontenttype;

    private String dataschema;

    private String subject;

    private String time;

    private String partitionkey;

    private String filesequencenumber;

    private String sequence;

    private String totalcount;

    public BDMOutboundCloudEventDetailsBuilder
      setSpecVersion(final String specversion) {

      this.specversion = specversion;
      return this;
    }

    public BDMOutboundCloudEventDetailsBuilder setId(final String id) {

      this.id = id;
      return this;
    }

    public BDMOutboundCloudEventDetailsBuilder
      setSource(final String source) {

      this.source = source;
      return this;
    }

    public BDMOutboundCloudEventDetailsBuilder setType(final String type) {

      this.type = type;
      return this;
    }

    public BDMOutboundCloudEventDetailsBuilder
      setDatacontenttype(final String datacontenttype) {

      this.datacontenttype = datacontenttype;
      return this;
    }

    public BDMOutboundCloudEventDetailsBuilder
      setDataschema(final String dataschema) {

      this.dataschema = dataschema;
      return this;
    }

    public BDMOutboundCloudEventDetailsBuilder
      setSubject(final String subject) {

      this.subject = subject;
      return this;
    }

    public BDMOutboundCloudEventDetailsBuilder setTime(final String time) {

      this.time = time;
      return this;
    }

    public BDMOutboundCloudEventDetailsBuilder
      setPartitionkey(final String partitionkey) {

      this.partitionkey = partitionkey;
      return this;
    }

    public BDMOutboundCloudEventDetailsBuilder
      setFilesequencenumber(final String filesequencenumber) {

      this.filesequencenumber = filesequencenumber;
      return this;
    }

    public BDMOutboundCloudEventDetailsBuilder
      setSequence(final String sequence) {

      this.sequence = sequence;
      return this;
    }

    public BDMOutboundCloudEventDetailsBuilder
      setTotalcount(final String totalcount) {

      this.totalcount = totalcount;
      return this;
    }

    public BDMOutboundCloudEventDetails build() {

      return new BDMOutboundCloudEventDetails(this);
    }
  }

}
