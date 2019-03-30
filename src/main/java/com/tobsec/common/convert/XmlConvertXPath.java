package com.tobsec.common.convert;

import com.tobsec.common.ConnectionBean;

import com.tobsec.common.convert.*;

import java.io.IOException;
import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathExpressionException;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * XML을 읽어와서 ConnectionBean으로 만들어줌.
 * XPATH 이용해서 변환
 */
public class XmlConvertXPath implements Converter {
    private String DEFAULT_CONFIG_FILE = "mysql_config.xml";
    private String configFile = DEFAULT_CONFIG_FILE;

    public void setConfigFile( String configFile ) {
        this.configFile = configFile;
    }
    
    public ConnectionBean makeConnBean() throws Exception {
        String className = "";
        String host = "";
        String databaseName = "";
        String userName = "";
        String userPass = "";

        ConnectionBean connBean = new ConnectionBean();

        try {
            ClassLoader classLoader = getClass().getClassLoader();

            File file = new File(classLoader.getResource(this.configFile).getFile());

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            DocumentBuilder documentBuilder = factory.newDocumentBuilder();

            Document document = documentBuilder.parse(file);

            XPath xpath = XPathFactory.newInstance().newXPath();

            Node classNameNode = (Node)xpath.evaluate("//*[@name='className']", document, XPathConstants.NODE);
            className = classNameNode.getAttributes().getNamedItem("value").getTextContent();
    
            Node hostNode = (Node)xpath.evaluate("//*[@name='host']", document, XPathConstants.NODE);
            host = hostNode.getAttributes().getNamedItem("value").getTextContent();
    
            Node databaseNameNode = (Node)xpath.evaluate("//*[@name='databaseName']", document, XPathConstants.NODE);
            databaseName = databaseNameNode.getAttributes().getNamedItem("value").getTextContent();
    
            Node userNameNode = (Node)xpath.evaluate("//*[@name='userName']", document, XPathConstants.NODE);
            userName = userNameNode.getAttributes().getNamedItem("value").getTextContent();
    
            Node userPassNode = (Node)xpath.evaluate("//*[@name='userPass']", document, XPathConstants.NODE);
            userPass = userPassNode.getAttributes().getNamedItem("value").getTextContent();

            connBean = new ConnectionBean(className, host, databaseName, userName, userPass);
        } catch(ParserConfigurationException | SAXException | IOException e) {
            throw new Exception(e);
        } catch(XPathExpressionException ie) {
            ie.printStackTrace();
        } 
        return connBean;
    }

}