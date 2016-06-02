package com.bigzhao.apk;

import android.content.Context;
import com.bigzhao.xml2axml.AxmlUtils;
import com.bigzhao.xml2axml.Encoder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

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

    public void merge(File unuse, File apk) throws Exception {
        FileUtils.deleteQuietly(ws);
        FileUtils.forceMkdir(ws);
        File loaderDir=new File(ws,"loader");
        ZipUtils.unzip(apk, loaderDir);
        FileUtils.deleteQuietly(new File(loaderDir, "META-INF"));
        File manifest=new File(loaderDir, "AndroidManifest.xml");
        String xml=AxmlUtils.decode(manifest);
        xml = processManifest(xml);
        FileUtils.writeStringToFile(new File(ws, "AndroidManifest.new.xml"), xml);
        byte[] bs=AxmlUtils.encode(xml);
        FileUtils.writeByteArrayToFile(manifest, bs);

        File dex=new File(loaderDir,"classes.dex");
        File dex2=new File("E:\\AndroidWS\\JianRMagicBox\\app\\build\\outputs\\apk\\classes.dex");
        mergeDex(dex,dex2,dex);

        ZipUtils.zip(loaderDir,new File(ws,apk.getName()));
    }

    public void bakSmali(File dex,File output) throws IOException {
        org.jf.baksmali.main.main(new String[]{"-o",output.getAbsolutePath(),dex.getAbsolutePath()});
    }

    public void smali(File input,File dex) throws IOException {
        org.jf.smali.main.main(new String[]{"-o",dex.getAbsolutePath(),input.getAbsolutePath()});
    }

    public void mergeDex(File dex1,File dex2,File outputDex) throws IOException {
        File smali=new File(ws,"smali");
        bakSmali(dex1, smali);
        File cocos2dxActivity=new File(smali,"org\\cocos2dx\\lib\\Cocos2dxActivity.smali");
        String code=FileUtils.readFileToString(cocos2dxActivity,"UTF-8");
        code=code.replace("invoke-static {v3}, Ljava/lang/System;->loadLibrary(Ljava/lang/String;)V",
                "invoke-static {v3}, Ljava/lang/System;->loadLibrary(Ljava/lang/String;)V\n" +
                "invoke-static {}, Lcom/bigzhao/jianrmagicbox/FadeCode;->loadNative()V");
        FileUtils.writeStringToFile(cocos2dxActivity,code);
        bakSmali(dex2, smali);
        if (outputDex.exists()) FileUtils.deleteQuietly(outputDex);
        smali(smali,outputDex);
    }

    private String processManifest(String xml) {
        xml=xml.replaceAll("package=\"[\\w\\.]+\"","package=\"com.bigzhao.jianrmagicbox\"");
        xml=xml.replaceAll("<intent-filter[\\s\\S]*?android.intent.category.LAUNCHER[\\s\\S]*?</intent-filter>","");
        xml=xml.replaceAll("(?=\\s*</application>)","<activity\n" +
                "\t\t\tandroid:theme=\"@android:01030007\"\n" +
                "\t\t\tandroid:name=\"com.bigzhao.jianrmagicbox.LoaderActivity\"\n" +
                "\t\t\tandroid:screenOrientation=\"6\"\n" +
                "\t\t\tandroid:configChanges=\"0x000004A0\"\n" +
                "\t\t\t>\n" +
                "\t\t\t android:exported=\"true\"\n" +
                "\n" +
                "\t\t\t<intent-filter\n" +
                "\t\t\t\t>\n" +
                "\t\t\t\t<action\n" +
                "\t\t\t\t\tandroid:name=\"android.intent.action.MAIN\"\n" +
                "\t\t\t\t\t>\n" +
                "\t\t\t\t</action>\n" +
                "\t\t\t\t<category\n" +
                "\t\t\t\t\tandroid:name=\"android.intent.category.LAUNCHER\"\n" +
                "\t\t\t\t\t>\n" +
                "\t\t\t\t</category>\n" +
                "\t\t\t</intent-filter>\n" +
                "\t\t</activity>");
        return xml;
    }


    private Document buildXmlDom(String xml) throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        return builder.parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));
    }
}
