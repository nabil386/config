<VIEW 
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd">
  
  <PAGE_PARAMETER NAME="taxSlipDataID"/>
  
  <SERVER_INTERFACE
    NAME="DISPLAYHISTORY"  
    CLASS="BDMTaxSlips"
    OPERATION="listHistoryByTaxSlip"  
  />
  
  <CONNECT>
    <SOURCE 
      NAME="PAGE" 
      PROPERTY="taxSlipDataID"/>
    <TARGET
      NAME="DISPLAYHISTORY"
      PROPERTY="key$taxSlipDataID"/> 
  </CONNECT>    
  
  <CLUSTER 
    TITLE="Cluster.Title.History">
    <LIST>  
      <FIELD
        LABEL="Field.Label.Category">
        <CONNECT>
          <SOURCE
            NAME="DISPLAYHISTORY"
            PROPERTY="slipTypeCode"
          />
        </CONNECT>
      </FIELD>
      <FIELD
        LABEL="Field.Label.IssuanceStatus">
        <CONNECT>
          <SOURCE
            NAME="DISPLAYHISTORY"
            PROPERTY="taxSlipStatusCode"
          />
        </CONNECT>
      </FIELD>
      <FIELD
        LABEL="Field.Label.ProcessingStatus">
        <CONNECT>
          <SOURCE
            NAME="DISPLAYHISTORY"
            PROPERTY="processingStatus"
          />
        </CONNECT>
      </FIELD>
      <FIELD
        LABEL="Field.Label.CreationMethod">
        <CONNECT>
          <SOURCE
            NAME="DISPLAYHISTORY"
            PROPERTY="creationMethodType"
          />
        </CONNECT>
      </FIELD>
      <FIELD
        LABEL="Field.Label.ProcessingDate">
        <CONNECT>
          <SOURCE
            NAME="DISPLAYHISTORY"
            PROPERTY="processingDate"
          />
        </CONNECT>
      </FIELD>
      <FIELD
        LABEL="Field.Label.User">
        <CONNECT>
          <SOURCE
            NAME="DISPLAYHISTORY"
            PROPERTY="createdBy"
          />
        </CONNECT>
      </FIELD>
      
      <DETAILS_ROW>
        <INLINE_PAGE PAGE_ID="BDMParticipant_viewTaxSlipHistoryInline">
          <CONNECT>
            <SOURCE
              NAME="DISPLAYHISTORY"
              PROPERTY="dtls$taxSlipDataID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="taxSlipDataID"
            />
          </CONNECT>
          
        </INLINE_PAGE>
      </DETAILS_ROW>
    </LIST>
  </CLUSTER>
    
</VIEW>