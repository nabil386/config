<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:output method="html" indent="yes"/>

  <xsl:template match="accesslevel">
    <html>
			<head>
				<style type="text/css">
				.list {
						width: 100%;
						border-style: solid;
						border-width: 1px;
						border-color: #BDCDE3;
				}
				.data, .dataLast {
						border-style: solid;
						border-width: 1px 1px 0 0;
						border-color: #BDCDE3;
						padding: 2px 5px 2px 5px;
				}
				.dataLast {
						border-width: 1px 0 0 0;
				}
				.label, .labelTop {
						border-style: solid;
						border-color: #BDCDE3;
						border-width: 1px 0 0 0;
						background-color: #EAF5FC;
						text-align: right;
						padding: 2px 5px 2px 5px;
				}
				.labelTop {
						border-width: 0;
				}
				.value, .valueTop {
						border-style: solid;
						border-color: #BDCDE3;
						border-width: 1px 0 0 1px;
						padding: 2px 5px 2px 5px;
				}
				.valueTop {
						border-width: 0 0 0 1px;
				}
				.singleValue {
						border-style: solid;
						border-color: #BDCDE3;
						border-width: 1px 0 0 0;
						padding: 2px 3px 3px 3px;
				}
				.heading, .headingLast {
				    border-style: solid;
				    border-width: 0 1px 0 0;
				    border-color: #BDCDE3;
				    background-color: #E4EBF4;
				    padding: 2px 5px 2px 5px;
				}
				.headingLast {
				    border-width: 0;
				}
				.listValueOdd {
						background-color: white;
				}

				.listValueEven {
						background-color: #EAF5FC;
				}
				</style>
			</head>

			<body>
        <a name="top"></a>
				<h1>Access Level Report</h1>
				<hr/>

				<xsl:apply-templates select="./results" mode="summary"/>
				<xsl:apply-templates select="./results" mode="classsummary"/>
				<xsl:apply-templates select="./results/class" mode="classdetail"/>
				<!--<xsl:apply-templates select="./searchpath" mode="searchpath"/>-->
      </body>
    </html>
  </xsl:template>

  <xsl:template match="results" mode="summary">
		<h2>Summary</h2>
		<table class="list" cellspacing="0">
			<colgroup span="2">
				<col width="25%"/>
				<col width="75%"/>
			</colgroup>
			<tbody>
				<tr>
					<td class="labelTop">References to restricted classes: </td>
					<td class="valueTop"><xsl:value-of select="./@totalrestrictedclasscount"/></td>
				</tr>
				<tr>
					<td class="label">References to internal classes: </td>
					<td class="value"><xsl:value-of select="./@totalinternalclasscount"/></td>
				</tr>
				<tr>
					<td class="label">Calls to restricted operations: </td>
					<td class="value"><xsl:value-of select="./@totalrestrictedoperationcount"/></td>
				</tr>
				<tr>
					<td class="label">Calls to internal operations: </td>
					<td class="value"><xsl:value-of select="./@totalinternaloperationcount"/></td>
				</tr>
			</tbody>
		</table>
  </xsl:template>

  <xsl:template match="results" mode="classsummary">
		<h2><xsl:value-of select="concat('Classes with access level violations (', count(./class), ')')"/></h2>
		<table class="list" cellspacing="0">
			<colgroup span="6"/>
			<thead>
				<tr>
					<td class="heading">Name</td>
					<td class="heading">Restricted Classes</td>
					<td class="heading">Internal Classes</td>
					<td class="heading">Restricted Operations</td>
					<td class="heading">Internal Operations</td>
					<td class="headingLast">Full Name</td>
				</tr>
			</thead>
			<tbody id="tableBodyClasses">
				<xsl:for-each select="./class">
					<tr>
						<xsl:choose>
							<xsl:when test="position() mod 2 = 1">
								<xsl:attribute name="class">listValueOdd</xsl:attribute>
							</xsl:when>
							<xsl:otherwise>
								<xsl:attribute name="class">listValueEven</xsl:attribute>
							</xsl:otherwise>
						</xsl:choose>
						<td class="data"><a href="{concat('#', ./@name)}"><xsl:value-of select="./@simplename"/></a></td>
						<td class="data"><xsl:value-of select="./@restrictedclasscount"/></td>
						<td class="data"><xsl:value-of select="./@internalclasscount"/></td>
						<td class="data"><xsl:value-of select="./@restrictedoperationcount"/></td>
						<td class="data"><xsl:value-of select="./@internaloperationcount"/></td>
						<td class="dataLast"><xsl:value-of select="./@name"/></td>
					</tr>
				</xsl:for-each>
			</tbody>
		</table>
  </xsl:template>

  <xsl:template match="class" mode="classdetail">
		<a name="{./@name}"/>
		<h3><xsl:value-of select="./@name"/><xsl:text>&#160;</xsl:text><a href="#top" style="text-decoration:none;">(back to top)</a></h3>
		<xsl:variable name="classrefcount" select="count(./classreference)"/>
		<xsl:variable name="opcallcount" select="count(./operationcall)"/>
		<xsl:if test="$classrefcount > 0">
			<h4><xsl:value-of select="concat('Class References (', $classrefcount, ')')"/></h4>
			<table class="list" cellspacing="0">
				<colgroup span="3"/>			
				<thead>
					<tr>
						<td class="heading">Access Level</td>
						<td class="heading">Name</td>
						<td class="headingLast">Full Name</td>
					</tr>
				</thead>
				<tbody>
					<xsl:for-each select="./classreference">
						<tr>
							<xsl:choose>
								<xsl:when test="position() mod 2 = 1">
									<xsl:attribute name="class">listValueOdd</xsl:attribute>
								</xsl:when>
								<xsl:otherwise>
									<xsl:attribute name="class">listValueEven</xsl:attribute>
								</xsl:otherwise>
							</xsl:choose>
							<td class="data"><xsl:value-of select="./@level"/></td>
							<td class="data"><xsl:value-of select="./@simplename"/></td>
							<td class="dataLast"><xsl:value-of select="./@name"/></td>
						</tr>
					</xsl:for-each>
			</tbody>
			</table>
		</xsl:if>
		<xsl:if test="$opcallcount > 0">
			<h4><xsl:value-of select="concat('Operation calls (', $opcallcount, ')')"/></h4>
			<table class="list" cellspacing="0">
				<colgroup span="5"/>
				<thead>
					<tr>
						<td class="heading">Access Level</td>
						<td class="heading">Class</td>
						<td class="heading">Operation</td>
						<td class="heading">Line</td>
						<td class="headingLast">Trace</td>
					</tr>
				</thead>
				<tbody>
					<xsl:for-each select="./operationcall">
						<tr>
							<xsl:choose>
								<xsl:when test="position() mod 2 = 1">
									<xsl:attribute name="class">listValueOdd</xsl:attribute>
								</xsl:when>
								<xsl:otherwise>
									<xsl:attribute name="class">listValueEven</xsl:attribute>
								</xsl:otherwise>
							</xsl:choose>
							<td class="data"><xsl:value-of select="./@level"/></td>
							<td class="data"><xsl:value-of select="./@declaringclass"/></td>
							<td class="data"><xsl:value-of select="./@operation"/></td>
							<td class="data"><xsl:value-of select="./@line"/></td>
							<td class="dataLast"><xsl:value-of select="./@trace"/></td>
						</tr>
					</xsl:for-each>
			</tbody>
			</table>
		</xsl:if>
		<br/>
		<hr/>
  </xsl:template>

  <xsl:template match="searchpath" mode="searchpath">
		<h2>Search Path</h2>
		<table class="list" cellspacing="0">
			<tbody>
				<xsl:for-each select="./pathelement">
					<tr>
						<xsl:choose>
							<xsl:when test="position() mod 2 = 1">
								<xsl:attribute name="class">listValueOdd</xsl:attribute>
							</xsl:when>
							<xsl:otherwise>
								<xsl:attribute name="class">listValueEven</xsl:attribute>
							</xsl:otherwise>
						</xsl:choose>
						<td class="singleValue" colspan="2">
							<xsl:value-of select="@name"/>
						</td>
					</tr>
				</xsl:for-each>
			</tbody>
		</table>
  </xsl:template>

</xsl:stylesheet>
