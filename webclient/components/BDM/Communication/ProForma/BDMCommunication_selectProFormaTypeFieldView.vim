<?xml version="1.0" encoding="UTF-8"?>
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


	<CLUSTER
    LABEL_WIDTH="25"
    NUM_COLS="2"
    TITLE="Cluster.Title.CommunicationType"
  >


    <FIELD
      LABEL="Field.Label.TemplateID"
      WIDTH="45"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="listProFormaTemplateByTypeAndParticipantKey$templateIDCode"
        />
      </CONNECT>
    </FIELD>
    
    <FIELD
      LABEL="Field.Label.TemplateName"
      WIDTH="45"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="listProFormaTemplateByTypeAndParticipantKey$templateName"
        />
      </CONNECT>
    </FIELD>
    
    <FIELD
      LABEL="Field.Label.Language"
      WIDTH="45"
    >
	<!-- TASK 8657 Select Language from Contact Preference -->
	<CONNECT>
		<SOURCE NAME="DISPLAYCONTACTPREF"
			PROPERTY="result$preferredLanguage" />
		<!-- TASK 8657 -->
	</CONNECT>

	<!-- TASK 8657 Select written Language from Contact Preference -->	
    

      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="listProFormaTemplateByTypeAndParticipantKey$language"
        />
      </CONNECT>
    </FIELD>
    
    <FIELD
      LABEL="Field.Label.Type"
      WIDTH="80"
	  USE_BLANK="true"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="listProFormaTemplateByTypeAndParticipantKey$dtls$templateType"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>

</VIEW>