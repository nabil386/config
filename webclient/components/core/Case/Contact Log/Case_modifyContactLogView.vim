<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012,2023. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- The included view to modify a contact log details. -->

<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>

  <PAGE_TITLE>
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="PageTitle.Title"
      />
    </CONNECT>
  </PAGE_TITLE>

  <SERVER_INTERFACE
    CLASS="ContactLog"
    NAME="DISPLAY"
    OPERATION="readContactLogForModify"
    PHASE="DISPLAY"
  />

  <SERVER_INTERFACE
    CLASS="Case"
    NAME="ACTION"
    OPERATION="modifyContactLog"
    PHASE="ACTION"
  />

  <SERVER_INTERFACE
    CLASS="IntegratedCase"
    NAME="DISPLAYCASEPARTICIPANT"
    OPERATION="listCaseParticipantsDetailsForContactLog"
  />
  
  <SERVER_INTERFACE
    CLASS="Case"
    NAME="DISPLAYCONCERNCASEPARTICIPANT"
    OPERATION="listContactLogConcernCaseParticipant"
  />
  
  <SERVER_INTERFACE
    CLASS="ContactLog"
    NAME="DISPLAYPURPOSE"
    OPERATION="listPurpose"
  />
  
  <SERVER_INTERFACE
    CLASS="Case"
    NAME="DISPLAY_SUBJECT"
    OPERATION="checkContactLogSubjectEnabled"
    PHASE="DISPLAY"
  />
  
  <PAGE_PARAMETER NAME="caseID"/>
  <PAGE_PARAMETER NAME="contactLogID"/>
  <PAGE_PARAMETER NAME="description"/>

  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="contactLogID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$contactLogID"
    />
  </CONNECT>

  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAYCASEPARTICIPANT"
      PROPERTY="listMemberDetailsKey$caseID"
    />
  </CONNECT>

  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAYCONCERNCASEPARTICIPANT"
      PROPERTY="key$caseID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="contactLogID"
    />
    <TARGET
      NAME="DISPLAYCONCERNCASEPARTICIPANT"
      PROPERTY="key$contactLogID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="caseID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="contactLogDetails$contactLogID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="contactLogID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="createdDateTime"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="createdDateTime"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="createdBy"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="createdBy"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="contactLogDetails$recordStatus"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="recordStatus"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="addendumInd"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="addendumInd"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="contactLogDetails$versionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="versionNo"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="narrativeEditableInd"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="narrativeEditableIndOpt"
    />
  </CONNECT>

  <CLUSTER
    LABEL_WIDTH="35"
    NUM_COLS="2"
  >

   <FIELD
      HEIGHT="4"
      LABEL="Field.Label.Purpose"
      USE_BLANK="false"
    >
      <CONNECT>
        <INITIAL
          HIDDEN_PROPERTY="purposeCode"
          NAME="DISPLAYPURPOSE"
          PROPERTY="purposeName"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="purpose"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="purpose"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Location">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="location"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="location"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.StartDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="startDateTime"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="startDateTime"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Type">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="contactLogType"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="contactLogType"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      HEIGHT="4"
      LABEL="Field.Label.ConcernCaseParticipant"
      USE_BLANK="true"
    >
      <CONNECT>
        <INITIAL
          HIDDEN_PROPERTY="caseParticipantRoleID"
          NAME="DISPLAYCASEPARTICIPANT"
          PROPERTY="name"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="DISPLAYCONCERNCASEPARTICIPANT"
          PROPERTY="caseParticipantTabList"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="concernCaseParticipantTabList"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Author">
      <CONNECT>
        <INITIAL
          NAME="DISPLAY"
          PROPERTY="authorFullName"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="author"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="author"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.LocationDescription">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="locationDescription"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="locationDescription"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.EndDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="endDateTime"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="endDateTime"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Method">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="method"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="method"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>

  <CLUSTER
    LABEL_WIDTH="30"
    NUM_COLS="1"
    SHOW_LABELS="true"
    TITLE="Cluster.Label.NarrativeDetails"
  >
    <!-- conditional cluster to display the subject, if feature enabled -->
  	<CLUSTER>
	    <CONDITION>
	      <IS_TRUE
	        NAME="DISPLAY_SUBJECT"
	        PROPERTY="result$contactLogSubjectEnabledInd"
	      />
	    </CONDITION>
	    
	    <FIELD
	     LABEL="Field.Label.Subject">
	      <CONNECT>
	        <SOURCE
	          NAME="DISPLAY"
	          PROPERTY="result$readDetails$contactLogDetails$subject"
	        />
	      </CONNECT>
	      <CONNECT>
	        <TARGET
	          NAME="ACTION"
	          PROPERTY="details$contactLogDetails$contactLogDetails$subject"
	        />
	      </CONNECT>
	    </FIELD>
    </CLUSTER>
    
    <!-- conditional cluster containing icon and message, if the narrative can be edited -->
    <CLUSTER>
    <CONDITION>
      <IS_TRUE
        NAME="DISPLAY"
        PROPERTY="narrativeEditableInd"
      />
    </CONDITION>
    <CONTAINER>
		    <IMAGE IMAGE="UnlockedIcon" LABEL="Icon.Editable"/>
		    <FIELD>
		    	  <CONNECT>
			        <SOURCE NAME="DISPLAY" PROPERTY="editableNarrativeMessageOpt" />
			      </CONNECT>
		    </FIELD>
		  </CONTAINER>
    </CLUSTER>
    
    <!-- conditional cluster containing icon, message, and read-only version of ckeditor, if the narrative is locked -->
    <CLUSTER>
	    <CONDITION>
	      <IS_TRUE
	        NAME="DISPLAY"
	        PROPERTY="narrativeLockedInd"
	      />
	    </CONDITION>
	    <CONTAINER>
		    <IMAGE IMAGE="LockedIcon" LABEL="Icon.Locked"/>
		    <FIELD>
		    	  <CONNECT>
			        <SOURCE NAME="DISPLAY" PROPERTY="editableNarrativeMessageOpt" />
			      </CONNECT>
		    </FIELD>
		</CONTAINER>
		<!-- read-only narrative field-->
		<FIELD CONFIG="curam_readonly_config.js">
			<LABEL>
	    	  <CONNECT>
		        <SOURCE NAME="DISPLAY" PROPERTY="narrativeFieldLabelTextOpt" />
		      </CONNECT>
		    </LABEL>
	      <CONNECT>
	        <TARGET
	          NAME="ACTION"
	          PROPERTY="noteDetails$notesText"
	        />
	      </CONNECT>
	    </FIELD>
    </CLUSTER>
    
    <CLUSTER>
    	<CONDITION>
	      <IS_FALSE
	        NAME="DISPLAY"
	        PROPERTY="narrativeLockedInd"
	      />
	    </CONDITION>
	    <!-- field where narrative is not read-only -->
	    <FIELD>
		    <LABEL>
	    	  <CONNECT>
		        <SOURCE NAME="DISPLAY" PROPERTY="narrativeFieldLabelTextOpt" />
		      </CONNECT>
		    </LABEL>
	      <CONNECT>
	        <SOURCE
	          NAME="DISPLAY"
	          PROPERTY="latestNarrativeText"
	        />
	      </CONNECT>
	      <CONNECT>
	        <TARGET
	          NAME="ACTION"
	          PROPERTY="noteDetails$notesText"
	        />
	      </CONNECT>
	    </FIELD>
    </CLUSTER>
    
    <!-- Narrative History -->
    <CLUSTER
    SHOW_LABELS="false"
    STYLE="RemoveFieldPadding"
	  >
	    <FIELD
	      HEIGHT="4"
	    >
	      <CONNECT>
	        <SOURCE
	          NAME="DISPLAY"
	          PROPERTY="narrativeHistoryOpt"
	        />
	      </CONNECT>
	    </FIELD>

    </CLUSTER>
    
  </CLUSTER>
  
</VIEW>
