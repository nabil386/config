<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:template match="/">  
       <html>
            <body>
                <xsl:for-each select="SummaryView/LinkedGroup">
                    <div class="cluster cluster-with-header collapse label-field"
                        onclick="toggleCluster(this,arguments[0]);">
                        <h2 class="collapse">
                            <xsl:value-of select="@Name"/>
                        </h2>
                        <table cellpadding="0" cellspacing="0">
                            <xsl:if test="count(Field) = 3">
                                <tbody>
                                    <xsl:for-each select="Field">
                                        <tr>
                                            <td class="label">
                                                <xsl:value-of select="Name"/>:
                                            </td>
                                            <td class="field" align="left">
                                                <xsl:value-of select="Value"/>
                                            </td>
                                        </tr>
                                    </xsl:for-each>
                                </tbody>
                            </xsl:if> 
                            <xsl:if test="not(count(Field)=3)">
                                <col width="30%"/>
                                <col width="20%"/>
                                <col width="30%"/>
                                <col width="20%"/>
                                <tbody>
                                    <xsl:for-each select="Field[position() mod 2 = 1]">                                    
                                        <tr>    
                                            <xsl:variable name="others"
                                                select="following-sibling::Field[position()&lt; 2]"/> 
                                            <td class="label">
                                                <xsl:value-of select="Name"/>:
                                            </td>
                                            <td class="field" align="left">
                                                <xsl:value-of select="Value"/>
                                            </td>
                                            <xsl:if test="($others)">
                                                <td class="label">
                                                    <xsl:value-of select="following-sibling::*/Name"/>
                                                    :
                                                </td>                                            
                                                <td class="field" align="left">
                                                    <xsl:value-of select="following-sibling::*/Value"/>
                                                </td>
                                                
                                            </xsl:if>
                                            <xsl:if test="not($others)">
                                                <td></td><td></td>
                                            </xsl:if>    
                                            
                                            
                                        </tr>
                                    </xsl:for-each>
                                    
                                </tbody>
                            </xsl:if> 
                        </table>
                    </div>
                    <div>
                        <table>
                            <p>
                                <br/>
                            </p>
                        </table>
                    </div>
                </xsl:for-each>


                <xsl:for-each select="SummaryView/SingletonGroup">
                    <div class="cluster cluster-with-header collapse label-field"
                        onclick="toggleCluster(this,arguments[0]);">
                        <h2 class="collapse">
                            <xsl:value-of select="@Name"/>
                        </h2>
                        <table cellpadding="0" cellspacing="0">
                            <xsl:if test="count(Field) = 3">
                                <tbody>
                                    <xsl:for-each select="Field">
                                    <tr>
                                    <td class="label">
                                        <xsl:value-of select="Name"/>:
                                    </td>
                                    <td class="field" align="left">
                                        <xsl:value-of select="Value"/>
                                    </td>
                                        </tr>
                                    </xsl:for-each>
                                </tbody>
                            </xsl:if> 
                            <xsl:if test="not(count(Field)=3)">
                                <col width="30%"/>
                                <col width="20%"/>
                                <col width="30%"/>
                                <col width="20%"/>
                            <tbody>
                                <xsl:for-each select="Field[position() mod 2 = 1]">                                    
                                    <tr>    
                                        <xsl:variable name="others"
                                            select="following-sibling::Field[position()&lt; 2]"/> 
                                        <td class="label">
                                                <xsl:value-of select="Name"/>:
                                            </td>
                                            <td class="field" align="left">
                                                <xsl:value-of select="Value"/>
                                            </td>
                                        <xsl:if test="($others)">
                                            <td class="label">
                                                <xsl:value-of select="following-sibling::*/Name"/>
                                               :
                                            </td>                                            
                                            <td class="field" align="left">
                                                <xsl:value-of select="following-sibling::*/Value"/>
                                            </td>
                                           
                                        </xsl:if>
                                         <xsl:if test="not($others)">
                                            <td></td><td></td>
                                            </xsl:if>    
                                            
                                       
                                    </tr>
                                </xsl:for-each>
                                
                            </tbody>
                            </xsl:if> 
                        </table>
                    </div>
                    <div>
                        <table>
                            <p>
                                <br/>
                            </p>
                        </table>
                    </div>
                </xsl:for-each>
                <xsl:for-each select="SummaryView/ListGroup">
                    <div class="list list-with-header collapse"
                        onclick="toggleCluster(this,arguments[0]);">
                        <h2 class="collapse">
                            <xsl:value-of select="@Name"/>
                        </h2>
                        <table cellspacing="0"
                            oncontextmenu="return
                        curam.listMenu?curam.listMenu.listClicked(this, arguments[0]): true;">                         
                            <col class="field"/>
                            <thead>
                                <tr>
                                    <xsl:for-each select="ListHeader/Heading">
                                        <th class="field">
                                            <xsl:value-of select="Name"/>
                                        </th>
                                    </xsl:for-each>
                                </tr>
                            </thead>
                            <tbody>
                                <xsl:for-each select="List">
                                    <tr>
                                        <xsl:if test="position() mod 2 = 1">
                                            <xsl:attribute name="class">odd
                                            clickable</xsl:attribute>
                                        </xsl:if>
                                        <xsl:if test="position() mod 2 = 0">
                                            <xsl:attribute name="class">even
                                            clickable</xsl:attribute>
                                        </xsl:if>
                                        <xsl:for-each select="Field">
                                            <xsl:choose>
                                                <xsl:when test="@url">
                                                  <td class="container actioncol">
                                                  <a href="{@url}">
                                                  <xsl:value-of select="Value"/>
                                                  </a>
                                                  </td>
                                                </xsl:when>
                                                <xsl:otherwise>
                                                  <td class="field">
                                                  <xsl:value-of select="Value"/>
                                                  </td>
                                                </xsl:otherwise>
                                            </xsl:choose>
                                        </xsl:for-each>
                                    </tr>
                                </xsl:for-each>
                            </tbody>
                        </table>
                    </div>
                    <div>
                        <table>
                            <p>
                                <br/>
                            </p>
                        </table>
                    </div>
                </xsl:for-each>
   </body>

        </html>
    </xsl:template>
</xsl:stylesheet>
