<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:variable name="first-node-id" select="/decision/rule-node[1]/@id"/>

  <xsl:template match="node()|@*">
    <xsl:copy>
      <xsl:apply-templates select="@*"/>
      <xsl:apply-templates/>
    </xsl:copy>
  </xsl:template>

  <xsl:template match="rule-node">
    <xsl:if test="@id = $first-node-id">      
      <root-node>        
        <xsl:apply-templates select="@*"/>        
      </root-node>
    </xsl:if>
    <xsl:choose>
      <!-- Ignore any groups or list groups without children. -->
      <xsl:when test="count(rule-node) = 0
                      and (@type = 'RULE_GROUP' or @type = 'RULE_LIST_GROUP'
                           or @type ='OBJECTIVE_GROUP'
                           or @type ='OBJECTIVE_LIST_GROUP')"/>
      <!-- Collapse any group/list group branches into a single one. -->
      <xsl:when test="count(rule-node) = 1
                      and not((@type ='OBJECTIVE' or @type ='OBJECTIVE_GROUP'
                               or @type ='OBJECTIVE_LIST_GROUP')
                              and not(ancestor::node()[@type = 'OBJECTIVE_LIST_GROUP'
                                                       or @type = 'OBJECTIVE_GROUP'
                                                       or @type = 'OBJECTIVE']))">
        <xsl:apply-templates select="rule-node" />
      </xsl:when>
      <xsl:otherwise>
        <xsl:copy>
          <xsl:apply-templates select="@*"/>
          <xsl:apply-templates/>
        </xsl:copy>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <xsl:template match="text()"/>
  
</xsl:stylesheet>