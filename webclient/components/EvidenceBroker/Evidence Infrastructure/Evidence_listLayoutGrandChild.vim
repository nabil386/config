<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright 2008, 2010 Curam Software Ltd.                               -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!--  List of grandchild evidence for a parent.                             -->
<VIEW xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd">
  <LIST DESCRIPTION="Cluster.Active.Description" TITLE="Cluster.Active.Title">
    <CONTAINER LABEL="List.Title.Action" SEPARATOR="Container.Separator" WIDTH="20">
      <ACTION_CONTROL LABEL="ActionControl.Label.View">
        <LINK PAGE_ID="Evidence_resolveViewEvidencePage">
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="caseID"/>
            <TARGET NAME="PAGE" PROPERTY="caseID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="activeList$dtls$evidenceID"/>
            <TARGET NAME="PAGE" PROPERTY="evidenceID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="activeList$dtls$evidenceType"/>
            <TARGET NAME="PAGE" PROPERTY="evidenceType"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="evidenceID"/>
            <TARGET NAME="PAGE" PROPERTY="parEvID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="evidenceType"/>
            <TARGET NAME="PAGE" PROPERTY="parEvType"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="parEvID"/>
            <TARGET NAME="PAGE" PROPERTY="grandParEvID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="parEvType"/>
            <TARGET NAME="PAGE" PROPERTY="grandParEvType"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="contextDescription"/>
            <TARGET NAME="PAGE" PROPERTY="contextDescription"/>
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      <ACTION_CONTROL LABEL="ActionControl.Label.Update">
        <LINK OPEN_MODAL="true" PAGE_ID="Evidence_resolveModifyEvidencePage">
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="caseID"/>
            <TARGET NAME="PAGE" PROPERTY="caseID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="activeList$dtls$evidenceID"/>
            <TARGET NAME="PAGE" PROPERTY="evidenceID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="activeList$dtls$evidenceType"/>
            <TARGET NAME="PAGE" PROPERTY="evidenceType"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="evidenceID"/>
            <TARGET NAME="PAGE" PROPERTY="parEvID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="evidenceType"/>
            <TARGET NAME="PAGE" PROPERTY="parEvType"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="parEvID"/>
            <TARGET NAME="PAGE" PROPERTY="grandParEvID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="parEvType"/>
            <TARGET NAME="PAGE" PROPERTY="grandParEvType"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="contextDescription"/>
            <TARGET NAME="PAGE" PROPERTY="contextDescription"/>
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      <ACTION_CONTROL LABEL="ActionControl.Label.Remove">
        <LINK OPEN_MODAL="true" PAGE_ID="Evidence_removeActive" WINDOW_OPTIONS="width=400,height=150">
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="activeList$dtls$evidenceDescriptorID"/>
            <TARGET NAME="PAGE" PROPERTY="evidenceDescriptorID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="contextDescription"/>
            <TARGET NAME="PAGE" PROPERTY="contextDescription"/>
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      <ACTION_CONTROL LABEL="ActionControl.Label.Attribution">
        <LINK PAGE_ID="Evidence_listPDAttribution">
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="activeList$dtls$evidenceDescriptorID"/>
            <TARGET NAME="PAGE" PROPERTY="evidenceDescriptorID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="activeList$dtls$evidenceType"/>
            <TARGET NAME="PAGE" PROPERTY="evidenceType"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="evidenceID"/>
            <TARGET NAME="PAGE" PROPERTY="evidenceID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="caseID"/>
            <TARGET NAME="PAGE" PROPERTY="caseID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="contextDescription"/>
            <TARGET NAME="PAGE" PROPERTY="contextDescription"/>
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </CONTAINER>
    <FIELD LABEL="List.Title.SharedStatus" WIDTH="8">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="activeList$dtls$sharedStatus"/>
      </CONNECT>
    </FIELD>
    <FIELD LABEL="List.Title.PendingIndication" WIDTH="3">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="activeList$dtls$wipPendingCode"/>
      </CONNECT>
    </FIELD>
    <FIELD LABEL="List.Title.Name" WIDTH="18">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="activeList$dtls$concernRoleName"/>
      </CONNECT>
    </FIELD>
    <FIELD LABEL="List.Title.EffectiveDate" WIDTH="13">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="activeList$dtls$effectiveFrom"/>
      </CONNECT>
    </FIELD>
    <FIELD LABEL="List.Title.Details" WIDTH="23">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="activeList$dtls$summary"/>
      </CONNECT>
    </FIELD>
  </LIST>
  <!-- BEGIN, CR00051602, ANK -->
  <CLUSTER DESCRIPTION="Cluster.WIP.Description" SHOW_LABELS="false" STYLE="evidence-workspace-cluster" TITLE="Cluster.WIP.Title">
    <!-- END, CR00051602 -->


    <!-- BEGIN, CR00050298, MR -->
    <!-- BEGIN, HARP 64908, SP -->
    <LIST DESCRIPTION="Cluster.WIP.Update.Description" TITLE="List.Title.NewAndUpdateList">
      <!-- END, HARP 64908 -->
      <!-- END, CR00050298 -->


      <CONTAINER LABEL="List.Title.Action" SEPARATOR="Container.Separator" WIDTH="18">
        <ACTION_CONTROL LABEL="ActionControl.Label.View">
          <LINK PAGE_ID="Evidence_resolveViewEvidencePage">
            <CONNECT>
              <SOURCE NAME="PAGE" PROPERTY="caseID"/>
              <TARGET NAME="PAGE" PROPERTY="caseID"/>
            </CONNECT>
            <CONNECT>
              <SOURCE NAME="DISPLAY" PROPERTY="newAndUpdateList$dtls$evidenceID"/>
              <TARGET NAME="PAGE" PROPERTY="evidenceID"/>
            </CONNECT>
            <CONNECT>
              <SOURCE NAME="DISPLAY" PROPERTY="newAndUpdateList$dtls$evidenceType"/>
              <TARGET NAME="PAGE" PROPERTY="evidenceType"/>
            </CONNECT>
            <CONNECT>
              <SOURCE NAME="PAGE" PROPERTY="evidenceID"/>
              <TARGET NAME="PAGE" PROPERTY="parEvID"/>
            </CONNECT>
            <CONNECT>
              <SOURCE NAME="PAGE" PROPERTY="evidenceType"/>
              <TARGET NAME="PAGE" PROPERTY="parEvType"/>
            </CONNECT>
            <CONNECT>
              <SOURCE NAME="PAGE" PROPERTY="parEvID"/>
              <TARGET NAME="PAGE" PROPERTY="grandParEvID"/>
            </CONNECT>
            <CONNECT>
              <SOURCE NAME="PAGE" PROPERTY="parEvType"/>
              <TARGET NAME="PAGE" PROPERTY="grandParEvType"/>
            </CONNECT>
            <CONNECT>
              <SOURCE NAME="PAGE" PROPERTY="contextDescription"/>
              <TARGET NAME="PAGE" PROPERTY="contextDescription"/>
            </CONNECT>
          </LINK>
        </ACTION_CONTROL>
        <ACTION_CONTROL LABEL="ActionControl.Label.Edit">
          <LINK OPEN_MODAL="true" PAGE_ID="Evidence_resolveModifyEvidencePage">
            <CONNECT>
              <SOURCE NAME="PAGE" PROPERTY="caseID"/>
              <TARGET NAME="PAGE" PROPERTY="caseID"/>
            </CONNECT>
            <CONNECT>
              <SOURCE NAME="DISPLAY" PROPERTY="newAndUpdateList$dtls$evidenceID"/>
              <TARGET NAME="PAGE" PROPERTY="evidenceID"/>
            </CONNECT>
            <CONNECT>
              <SOURCE NAME="DISPLAY" PROPERTY="newAndUpdateList$dtls$evidenceType"/>
              <TARGET NAME="PAGE" PROPERTY="evidenceType"/>
            </CONNECT>
            <CONNECT>
              <SOURCE NAME="PAGE" PROPERTY="evidenceID"/>
              <TARGET NAME="PAGE" PROPERTY="parEvID"/>
            </CONNECT>
            <CONNECT>
              <SOURCE NAME="PAGE" PROPERTY="evidenceType"/>
              <TARGET NAME="PAGE" PROPERTY="parEvType"/>
            </CONNECT>
            <CONNECT>
              <SOURCE NAME="PAGE" PROPERTY="parEvID"/>
              <TARGET NAME="PAGE" PROPERTY="grandParEvID"/>
            </CONNECT>
            <CONNECT>
              <SOURCE NAME="PAGE" PROPERTY="parEvType"/>
              <TARGET NAME="PAGE" PROPERTY="grandParEvType"/>
            </CONNECT>
            <CONNECT>
              <SOURCE NAME="PAGE" PROPERTY="contextDescription"/>
              <TARGET NAME="PAGE" PROPERTY="contextDescription"/>
            </CONNECT>
          </LINK>
        </ACTION_CONTROL>
        <ACTION_CONTROL LABEL="ActionControl.Label.Discard">
          <LINK OPEN_MODAL="true" PAGE_ID="Evidence_discardPendingUpdate" WINDOW_OPTIONS="width=700,height=150">
            <CONNECT>
              <SOURCE NAME="DISPLAY" PROPERTY="newAndUpdateList$dtls$evidenceDescriptorID"/>
              <TARGET NAME="PAGE" PROPERTY="evidenceDescriptorID"/>
            </CONNECT>
            <CONNECT>
              <SOURCE NAME="PAGE" PROPERTY="contextDescription"/>
              <TARGET NAME="PAGE" PROPERTY="contextDescription"/>
            </CONNECT>
          </LINK>
        </ACTION_CONTROL>
      </CONTAINER>
      <FIELD LABEL="List.Title.SharedStatus" WIDTH="8">
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="newAndUpdateList$dtls$sharedStatus"/>
        </CONNECT>
      </FIELD>
      <FIELD LABEL="List.Title.InEditIndication" WIDTH="3">
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="newAndUpdateList$dtls$wipPendingCode"/>
        </CONNECT>
      </FIELD>
      <FIELD LABEL="List.Title.Name" WIDTH="20">
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="newAndUpdateList$dtls$concernRoleName"/>
        </CONNECT>
      </FIELD>
      <FIELD LABEL="List.Title.EffectiveDate" WIDTH="13">
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="newAndUpdateList$dtls$effectiveFrom"/>
        </CONNECT>
      </FIELD>
      <FIELD LABEL="List.Title.Details" WIDTH="25">
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="newAndUpdateList$dtls$summary"/>
        </CONNECT>
      </FIELD>
      <FIELD LABEL="List.Title.UpdatedBy" WIDTH="10">
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="newAndUpdateList$dtls$updatedBy"/>
        </CONNECT>
      </FIELD>
    </LIST>


    <!-- BEGIN, CR00050298, MR -->
    <!-- BEGIN, HARP 64908, SP -->
    <LIST DESCRIPTION="Cluster.WIP.Remove.Description" TITLE="List.Title.RemovalList">
      <!-- END, HARP 64908 -->
      <!-- END, CR00050298 -->


      <CONTAINER LABEL="List.Title.Action" SEPARATOR="Container.Separator" WIDTH="18">
        <ACTION_CONTROL LABEL="ActionControl.Label.Undo">
          <LINK OPEN_MODAL="true" PAGE_ID="Evidence_discardPendingRemoveForActive" WINDOW_OPTIONS="width=400,height=150">
            <CONNECT>
              <SOURCE NAME="DISPLAY" PROPERTY="removeList$dtls$evidenceDescriptorID"/>
              <TARGET NAME="PAGE" PROPERTY="evidenceDescriptorID"/>
            </CONNECT>
            <CONNECT>
              <SOURCE NAME="PAGE" PROPERTY="contextDescription"/>
              <TARGET NAME="PAGE" PROPERTY="contextDescription"/>
            </CONNECT>
            <CONNECT>
              <SOURCE NAME="DISPLAY" PROPERTY="removeList$dtls$versionNo"/>
              <TARGET NAME="PAGE" PROPERTY="versionNo"/>
            </CONNECT>
          </LINK>
        </ACTION_CONTROL>
      </CONTAINER>
      <FIELD LABEL="List.Title.SharedStatus" WIDTH="8">
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="removeList$dtls$sharedStatus"/>
        </CONNECT>
      </FIELD>
      <FIELD LABEL="List.Title.Name" WIDTH="21">
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="removeList$dtls$concernRoleName"/>
        </CONNECT>
      </FIELD>
      <FIELD LABEL="List.Title.EffectiveDate" WIDTH="13">
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="removeList$dtls$effectiveFrom"/>
        </CONNECT>
      </FIELD>
      <FIELD LABEL="List.Title.Details" WIDTH="25">
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="removeList$dtls$summary"/>
        </CONNECT>
      </FIELD>
    </LIST>
  </CLUSTER>
  <!-- BEGIN, CR00021231, SP -->
  <!-- BEGIN, HARP 65364 -->
  <INCLUDE FILE_NAME="VerificationApplication_listEvidenceVerificationRequirements.vim"/>
  <!-- END, HARP 65364 -->
  <!-- END, CR00021231 -->
</VIEW>