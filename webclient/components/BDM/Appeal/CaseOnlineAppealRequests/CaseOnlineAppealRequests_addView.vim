<?xml version="1.0" encoding="UTF-8"?>
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <CLUSTER
    DESCRIPTION="Cluster.SingleSelect.Description"
    TITLE="Cluster.SingleSelect.Title"
  >

    <LIST>
      <ACTION_SET TYPE="LIST_ROW_MENU">
        <ACTION_CONTROL LABEL="ActionControl.Label.View">
          <LINK
            URI_SOURCE_NAME="DISPLAY1"
            URI_SOURCE_PROPERTY="result$dtls$requestURI"
          />
        </ACTION_CONTROL>
      </ACTION_SET>

      <CONTAINER
        ALIGNMENT="CENTER"
        WIDTH="5"
      >
        <WIDGET TYPE="SINGLESELECT">
          <WIDGET_PARAMETER NAME="SELECT_SOURCE">
            <CONNECT>
              <SOURCE
                NAME="DISPLAY1"
                PROPERTY="result$dtls$requestID"
              />
            </CONNECT>
          </WIDGET_PARAMETER>
          <WIDGET_PARAMETER NAME="SELECT_TARGET">
            <CONNECT>
              <TARGET
                NAME="ACTION"
                PROPERTY="onlineAppealRequestID"
              />
            </CONNECT>
          </WIDGET_PARAMETER>
        </WIDGET>
      </CONTAINER>
      <FIELD LABEL="Field.Label.RequestReferenceNumber">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY1"
            PROPERTY="result$dtls$reference"
          />
        </CONNECT>
      </FIELD>
      <FIELD LABEL="Field.Label.Program">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY1"
            PROPERTY="result$dtls$program"
          />
        </CONNECT>
      </FIELD>
      <FIELD LABEL="Field.Label.Name">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY1"
            PROPERTY="result$dtls$title"
          />
        </CONNECT>
      </FIELD>
      <FIELD LABEL="Field.Label.Appellant">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY1"
            PROPERTY="result$dtls$primaryAppellant"
          />
        </CONNECT>
      </FIELD>
    </LIST>


  </CLUSTER>


</VIEW>
