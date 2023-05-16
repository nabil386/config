<xsl:stylesheet version = '1.0' xmlns:xsl='http://www.w3.org/1999/XSL/Transform'>

  <xsl:output method="text"/>
  <xsl:strip-space elements="*"/>

  <xsl:template match="/MODEL">

    <xsl:text>(object Petal
    </xsl:text>
    <xsl:text>    version    	46
    </xsl:text>
    <xsl:text>    _written   	"Rose 7.7.0204.3001"
    </xsl:text>
    <xsl:text>    charSet    	0)
    </xsl:text>
    <xsl:text>
    </xsl:text>

    <xsl:text>(object Class_Category </xsl:text>"<xsl:value-of select="/MODEL/PACKAGE/@NAME" />"
    <xsl:text>    is_unit    	TRUE
    </xsl:text>
    <xsl:text>    is_loaded  	TRUE
    </xsl:text>
    <xsl:text>    quid       	</xsl:text>"<xsl:value-of select="/MODEL/PACKAGE/@QUID" />"
    <xsl:text>    exportControl 	"Public"
    </xsl:text>
    <xsl:text>    logical_models 	(list unit_reference_list</xsl:text>

    <xsl:apply-templates />

    <xsl:text>)
    </xsl:text>
    <xsl:text>    logical_presentations 	(list unit_reference_list))</xsl:text>

  </xsl:template>

  <xsl:template match="/MODEL/CLASS[@STEREOTYPE='wsconnector']">

    <xsl:text>
    </xsl:text>
    <xsl:text>    (object Class </xsl:text>"<xsl:value-of select="./@NAME" />"
    <xsl:text>        quid       	</xsl:text>"<xsl:value-of select="./@QUID" />"
    <xsl:text>        documentation	</xsl:text>
<xsl:text>
</xsl:text>
   <xsl:for-each select="./CLASS.OPTION" >
     <xsl:text>|</xsl:text><xsl:value-of select="./@NAME" /><xsl:text>=</xsl:text><xsl:value-of select="./@VALUE" />
<xsl:text>
</xsl:text>
   </xsl:for-each>
<xsl:text>|

</xsl:text>

    <xsl:text>        stereotype 	"wsconnector"
    </xsl:text>
    <xsl:text>        operations 	(list Operations</xsl:text>

    <xsl:for-each select="./CLASS.OPERATION">
      <xsl:text>
      </xsl:text>
      <xsl:text>            (object Operation </xsl:text>"<xsl:value-of select="./@NAME" />"
      <xsl:text>                attributes 	(list Attribute_Set
      </xsl:text>
      <xsl:text>                    (object Attribute
      </xsl:text>
      <xsl:text>                        tool       	"IDL"
      </xsl:text>
      <xsl:text>                        name       	"Raises"
      </xsl:text>
      <xsl:text>                        value      	""))
      </xsl:text>
      <xsl:text>                quid       	</xsl:text>"<xsl:value-of select="./@QUID" />"
      <xsl:text>                parameters 	(list Parameters</xsl:text>

      <xsl:for-each select="./CLASS.OPERATION.PARAMETER">
        <xsl:text>
        </xsl:text>
        <xsl:text>                    (object Parameter </xsl:text>"<xsl:value-of select="./@NAME" />"
        <xsl:text>                        quid       	</xsl:text>"<xsl:value-of select="./@QUID" />"
        <xsl:text>                        type       	</xsl:text>"<xsl:value-of select="./@TYPE" />
        <xsl:text>")</xsl:text>
      </xsl:for-each>

      <xsl:text>)
      </xsl:text>

      <xsl:if test="./@TYPE != ''">
        <xsl:text>                result     	</xsl:text>"<xsl:value-of select="./@TYPE" />"
      </xsl:if>
      <xsl:text>                concurrency 	"Sequential"
      </xsl:text>
      <xsl:text>                opExportControl 	"Public"
      </xsl:text>
      <xsl:text>                uid        	0</xsl:text>
      <xsl:text>)</xsl:text>

    </xsl:for-each>

    <xsl:text>))</xsl:text>

  </xsl:template>

  <xsl:template match="/MODEL/CLASS[@STEREOTYPE='struct']">

    <xsl:text>
    </xsl:text>
    <xsl:text>    (object Class </xsl:text>"<xsl:value-of select="./@NAME" />"
    <xsl:text>        quid       	</xsl:text>"<xsl:value-of select="./@QUID" />"
    <xsl:text>        stereotype 	"struct"
    </xsl:text>
    <xsl:text>        class_attributes 	(list class_attribute_list</xsl:text>

    <xsl:for-each select="./CLASS.ATTRIBUTE">
      <xsl:text>
      </xsl:text>
      <xsl:text>            (object ClassAttribute </xsl:text>"<xsl:value-of select="./@NAME" />"
      <xsl:text>                quid       	</xsl:text>"<xsl:value-of select="./@QUID" />"
      <xsl:text>                type       	</xsl:text>"<xsl:value-of select="./@TYPE" />"
      <xsl:text>                exportControl 	"Public")</xsl:text>
    </xsl:for-each>

    <xsl:text>))</xsl:text>

    <xsl:apply-templates />

  </xsl:template>

  <xsl:template match="/MODEL/CLASS/CLASS.ASSOCIATION">
    <xsl:text>
    </xsl:text>
    <xsl:text>    (object Association </xsl:text>"$UNNAMED$<xsl:number format="1" />"
    <xsl:text>        quid       	</xsl:text>"<xsl:value-of select="./@QUID" />"
    <xsl:text> 	      roles      	(list role_list</xsl:text>

    <xsl:for-each select="./CLASS.ASSOCIATION.ROLE">
      <xsl:text>
      </xsl:text>
      <xsl:if test="string-length(./@NAME) = 0">
        <xsl:text>            (object Role </xsl:text>"$UNNAMED$<xsl:number format="1" />"
      </xsl:if>
      <xsl:if test="string-length(./@NAME) != 0">
        <xsl:text>            (object Role </xsl:text>"<xsl:value-of select="./@NAME" />"
      </xsl:if>
      <xsl:text>                quid       	</xsl:text>"<xsl:value-of select="./@QUID" />"
      <xsl:text>                quidu       	</xsl:text>"<xsl:value-of select="./@QUIDU" />"
      <xsl:text>                client_cardinality 	(value cardinality </xsl:text>"<xsl:value-of select="./@CARDINALITY" />")
      <xsl:if test="./@AGGREGATE = 'true'">
        <xsl:text>                is_aggregate 	TRUE</xsl:text>
      </xsl:if>
      <xsl:if test="string-length(./@NAME) != 0">
        <xsl:text>                label      	</xsl:text>"<xsl:value-of select="./@NAME" />"
      </xsl:if>
      <xsl:if test="./@CONTAINMENT = 'ByValue'">
        <xsl:text>                Containment 	"By Value"</xsl:text>
      </xsl:if>
      <xsl:text>)
      </xsl:text>
    </xsl:for-each>

    <xsl:text>))</xsl:text>

  </xsl:template>

</xsl:stylesheet>
