<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:exsl="http://exslt.org/common"
                extension-element-prefixes="exsl">
    <xsl:output method="xml" indent="no" standalone="no"
                encoding="utf-8" omit-xml-declaration="yes" />

    <xsl:param name="default.publisher"
               select="'Санкт-Петербургский политехнический университет Петра Великого'" />

    <xsl:template match='record'>
        <file xmlns="http://bibtexml.sf.net/">
            <xsl:call-template name="personalCreatorName" />
            <xsl:value-of select="subfield[@id='p']"/>
            <entry id="value" >
                <author>
                    <xsl:choose>
                        <xsl:when
                                test="field[@id='700' or @id='701'] or field[@id='461' or @id='463']/subfield[@id='1']/field[@id='700' or @id='701'] or field[@id='710' or @id='711'] or field[@id='461' or @id='463']/subfield[@id='1']/field[@id='710' or @id='711']">
                            <xsl:for-each
                                    select="field[@id='700' or @id='701'] | field[@id='461' or @id='463']/subfield[@id='1']/field[@id='700' or @id='701']">
                                <xsl:call-template name="personalCreator" />
                            </xsl:for-each>
                            <xsl:for-each
                                    select="field[@id='710' or @id='711'] | field[@id='461' or @id='463']/subfield[@id='1']/field[@id='710' or @id='711']">
                                <xsl:call-template name="corporateCreator" />
                            </xsl:for-each>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:choose>
                                <xsl:when
                                        test="field[@id='702'] or field[@id='461' or @id='463']/subfield[@id='1']/field[@id='702']">
                                    <xsl:for-each
                                            select="(field[@id='702'] | field[@id='461' or @id='463']/subfield[@id='1']/field[@id='702'])[1]">
                                        <xsl:call-template name="personalCreator" />
                                    </xsl:for-each>
                                </xsl:when>
                                <xsl:when
                                        test="field[@id='712'] or field[@id='461' or @id='463']/subfield[@id='1']/field[@id='712']">
                                    <xsl:for-each
                                            select="(field[@id='712'] | field[@id='461' or @id='463']/subfield[@id='1']/field[@id='712'])[1]">
                                        <xsl:call-template name="corporateCreator" />
                                    </xsl:for-each>
                                </xsl:when>
                            </xsl:choose>
                        </xsl:otherwise>
                    </xsl:choose>
                </author>


                <title>
                    <xsl:value-of select="field[@id='200']/subfield[@id='a']" />
                    <xsl:for-each select="field[@id='200']/subfield[@id='h' or @id='i']">
                        <xsl:choose>
                            <xsl:when test="@id='h'">
                                <xsl:text>. </xsl:text>
                                <xsl:value-of select="." />
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:choose>
                                    <xsl:when test="preceding-sibling::subfield[1]/@id='h'">
                                        <xsl:text>, </xsl:text>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:text>. </xsl:text>
                                    </xsl:otherwise>
                                </xsl:choose>
                                <xsl:value-of select="." />
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:for-each>
                </title>

                <year>
                    <xsl:choose>
                        <xsl:when test="field[@id='210']/subfield[@id='d']">
                            <xsl:value-of select="field[@id='210']/subfield[@id='d'][1]" />
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:choose>
                                <xsl:when
                                        test="field[@id='463']/subfield[@id='1']/field[@id='210']/subfield[@id='d']">
                                    <xsl:value-of
                                            select="field[@id='463']/subfield[@id='1']/field[@id='210']/subfield[@id='d'][1]" />
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:text>Unknown</xsl:text>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:otherwise>
                    </xsl:choose>
                </year>


                <language>
                    <xsl:if
                            test="field[@id='101']/subfield[@id='a'] or field[@id='461']/subfield[@id='1']/field[@if='101'] or field[@id='463']/subfield[@id='1']/field[@id='101']">
                        <xsl:choose>
                            <xsl:when test="field[@id='101']/subfield[@id='a'] = 'rus'">
                                    <xsl:text>russian</xsl:text>
                            </xsl:when>
                            <xsl:when test="field[@id='461'] and subfield[@id='1']/field[@if='101']/subfield[@id='a'] ='rus'">
                                <xsl:text>russian</xsl:text>
                            </xsl:when>
                            <xsl:when test="field[@id='463'] and subfield[@id='1'] and field[@id='101']/subfield[@id='a'] ='rus'">
                                <xsl:text>russian</xsl:text>
                            </xsl:when>
                        </xsl:choose>
                    </xsl:if>
                </language>


                <publisher>
                    <xsl:choose>
                        <xsl:when test="field[@id='210']/subfield[@id='c' or @id='g']">
                            <xsl:value-of select="field[@id='210']/subfield[@id='c' or @id='g'][1]" />
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:choose>
                                <xsl:when
                                        test="field[@id='463']/subfield[@id='1']/field[@id='210']/subfield[@id='c' or @id='g']">
                                    <xsl:value-of
                                            select="field[@id='463']/subfield[@id='1']/field[@id='210']/subfield[@id='c' or @id='g'][1]" />
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:value-of select="$default.publisher" />
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:otherwise>
                    </xsl:choose>
                </publisher>
            </entry>
        </file>
    </xsl:template>



    <xsl:template name="personalCreator">
        <xsl:call-template name="personalCreatorName" />
        <xsl:for-each select="subfield[@id='p']">
            <affiliation>
                <xsl:value-of select="." />
            </affiliation>
        </xsl:for-each>
    </xsl:template>



    <xsl:template name="personalCreatorName">
        <xsl:value-of select="subfield[@id='a']" />
        <xsl:if test="subfield[@id='d']">
            <xsl:text> </xsl:text>
            <xsl:value-of select="subfield[@id='d']" />
        </xsl:if>
        <xsl:choose>
            <xsl:when test="subfield[@id='g']">
                <xsl:text>, </xsl:text>
                <xsl:value-of select="subfield[@id='g']" />
            </xsl:when>
            <xsl:otherwise>
                <xsl:if test="subfield[@id='b']">
                    <xsl:text> </xsl:text>
                    <xsl:value-of select="subfield[@id='b']" />
                    <xsl:text> and </xsl:text>
                </xsl:if>
            </xsl:otherwise>
        </xsl:choose>
        <xsl:if test="subfield[@id='c']">
            <xsl:text> (</xsl:text>
            <xsl:for-each select="subfield[@id='c']">
                <xsl:value-of select="." />
                <xsl:if test="position() != last()">
                    <xsl:text>;</xsl:text>
                </xsl:if>
            </xsl:for-each>
            <xsl:text>) </xsl:text>
        </xsl:if>
    </xsl:template>

    <xsl:template name="corporateCreator">
        <xsl:value-of select="subfield[@id='a']" />
        <xsl:for-each select="subfield[@id='b']">
            <xsl:text>. </xsl:text>
            <xsl:value-of select="." />
        </xsl:for-each>
    </xsl:template>

</xsl:stylesheet>