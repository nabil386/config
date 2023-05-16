<?xml version="1.0" encoding="UTF-8"?>
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This process allows the user to modify user skill details.             -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <PAGE_TITLE>
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="PageTitle.StaticText1"
      />
    </CONNECT>
  </PAGE_TITLE>


  <SERVER_INTERFACE
    CLASS="Organization"
    NAME="DISPLAY"
    OPERATION="viewUserSkill"
    PHASE="DISPLAY"
  />


  <SERVER_INTERFACE
    CLASS="BDMOrganization"
    NAME="ACTION"
    OPERATION="modifyUserSkill"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="userSkillID"/>
  <PAGE_PARAMETER NAME="pageDescription"/>
    
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="userSkillID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="userSkillKey$userSkillID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="userSkillID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="modifyUserSkillDetails$userSkillID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="recordStatus"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="recordStatus"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="userName"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="userName"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="versionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="modifyUserSkillDetails$versionNo"
    />
  </CONNECT>


  <CLUSTER LABEL_WIDTH="32">
    
    <FIELD
      LABEL="Field.Label.SkillType"
      WIDTH="80"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="skillType"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="skillType"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.SkillLevel"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="skillLevel"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Description"
  >
    <!-- BEGIN, CR00463142, EC -->
    <FIELD
      HEIGHT="4"
      LABEL="Cluster.Title.Description"
    >
      <!-- END, CR00463142 -->
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="userSkillDetails$details$description"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="modifyUserSkillDetails$description"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


</VIEW>
