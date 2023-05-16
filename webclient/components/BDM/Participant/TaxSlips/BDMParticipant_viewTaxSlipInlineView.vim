<VIEW 
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd">
  
  <PAGE_PARAMETER NAME="taxSlipDataID"/>
  
  <SERVER_INTERFACE
    NAME="DISPLAY"  
    CLASS="BDMTaxSlips"
    OPERATION="viewTaxSlipInline"  
  />
  
  <CONNECT>
    <SOURCE 
      NAME="PAGE" 
      PROPERTY="taxSlipDataID"/>
    <TARGET
      NAME="DISPLAY"
      PROPERTY="taxSlipDataID"/> 
  </CONNECT>    
  
  <CLUSTER 
    NUM_COLS="2"
    TITLE="Cluster.Title.RecipientDetails">
    <FIELD
      LABEL="Field.Label.LastName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="recipientSurName"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.FirstName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="recipientFirstName"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.Initials">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="recipientInitial"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.SocialInsuranceNumber">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="recipientSIN"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.MailingAddress">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="mailingAddress"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
  
  <CLUSTER 
    TITLE="Cluster.Title.Details"
  >
    <LIST>
      <FIELD
        LABEL="Field.Label.BoxNumber">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="boxID"
          />
        </CONNECT>
      </FIELD>
      <FIELD
        LABEL="Field.Label.BoxName">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="boxName"
          />
        </CONNECT>
      </FIELD>
      <FIELD
        LABEL="Field.Label.BoxValue">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="attrValue"
          />
        </CONNECT>
      </FIELD>
    </LIST>
  </CLUSTER>
    
</VIEW>