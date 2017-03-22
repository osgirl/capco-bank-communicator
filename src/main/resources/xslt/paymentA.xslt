<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <!-- Output with indentation. -->
    <xsl:output method="xml" encoding="UTF-8" omit-xml-declaration="no" indent="yes"/>
    
    <xsl:template match="/">
        <Document>
            <payment>
                <version><xsl:value-of select="local-name(Document/pain.001.001.02)"/></version>

                <xsl:for-each select="Document/pain.001.001.02">
                    <bank><xsl:value-of select="bank"/></bank>
                    <account><xsl:value-of select="account"/></account>
                    <debit><xsl:value-of select="debit"/></debit>
                    <credit><xsl:value-of select="credit"/></credit>
                </xsl:for-each>
            </payment>
        </Document>
    </xsl:template>

</xsl:stylesheet>