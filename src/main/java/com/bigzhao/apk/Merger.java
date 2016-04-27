package com.bigzhao.apk;

import android.content.Context;
import com.bigzhao.xml2axml.AxmlUtils;
import com.bigzhao.xml2axml.Encoder;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParserException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;

/**
 * Created by Roy on 16-4-27.
 */
public class Merger {

    private File ws;
    public Merger(File workspace){
        ws=workspace;
    }

    public void merge(File loader, File loadee) throws Exception {
        File loaderDir=new File(ws,"loader");
        File loadeeDir=new File(ws,"loadee");
        ZipUtils.unzip(loader,loaderDir);
        ZipUtils.unzip(loadee,loadeeDir);

        String xml=AxmlUtils.decode(new File(loaderDir, "AndroidManifest.xml"));
        Document d1=buildXmlDom(xml);
        

    }


    private Document buildXmlDom(String xml) throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        return builder.parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));
    }
}
