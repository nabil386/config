<?xml version="1.0" encoding="UTF-8"?>
<VIEW xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd">

   <SERVER_INTERFACE CLASS="BDMCommunicationStatusHistory" NAME="DISPLAY" OPERATION="readHistoryByCommunicationID" />
   
   <PAGE_PARAMETER NAME="communicationID" />
   
   <CONNECT>
      <SOURCE NAME="PAGE" PROPERTY="communicationID" />
      <TARGET NAME="DISPLAY" PROPERTY="communicationID" />
   </CONNECT>
   
   <CLUSTER>
      <LIST>
         <FIELD LABEL="Field.Label.UpdatedBy" WIDTH="60">
            <CONNECT>
               <SOURCE NAME="DISPLAY" PROPERTY="userName" />
            </CONNECT>
         </FIELD>
         <FIELD LABEL="Field.Label.DateTime" WIDTH="60">
            <CONNECT>
               <SOURCE NAME="DISPLAY" PROPERTY="statusDateTime" />
            </CONNECT>
         </FIELD>
         <FIELD LABEL="Field.Label.CommunicationStatus" WIDTH="30">
            <CONNECT>
               <SOURCE NAME="DISPLAY" PROPERTY="communicationStatus" />
            </CONNECT>
         </FIELD>
         <FIELD LABEL="Field.Label.Status" WIDTH="60">
            <CONNECT>
               <SOURCE NAME="DISPLAY" PROPERTY="recordStatus" />
            </CONNECT>
         </FIELD>
      </LIST>
   </CLUSTER>
</VIEW>