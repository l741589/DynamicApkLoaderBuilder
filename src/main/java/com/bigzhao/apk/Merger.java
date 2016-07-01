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
        System.out.println(ws);
        System.out.println(apk);
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
        File dex2=new File("E:\\AndroidWS\\JianRMagicBox\\app\\build\\outputs\\apk\\e\\classes.dex");

        File moduleDir=new File(loaderDir,"assets\\MagicBox\\module");

        FileUtils.copyDirectory(
                new File("E:\\IntellijWS\\DynamicApkLoaderBuilder\\src\\main\\assets"),
                new File(loaderDir, "assets"));
        FileUtils.copyFile(new File("E:\\Projects\\Project1\\Project1\\libs\\armeabi\\libMagicBox.so"),
                new File(moduleDir, "libMagicBox.so"));

        mergeDex(dex, dex2, dex, new File(moduleDir, "classes.dex"));
        //ZipUtils.zip(new File(moduleDir, "classes.dex"), new File(moduleDir, "classes.jar"));
        ZipUtils.zip(loaderDir, new File(ws, apk.getName()));
    }

    public void bakSmali(File dex,File output) throws IOException {
        org.jf.baksmali.main.main(new String[]{"-o",output.getAbsolutePath(),dex.getAbsolutePath()});
    }

    public void smali(File input,File dex) throws IOException {
        org.jf.smali.main.main(new String[]{"-o",dex.getAbsolutePath(),input.getAbsolutePath()});
    }

    public void mergeDex(File dex1,File dex2,File outputDex,File moduleDex) throws IOException {
        File smali=new File(ws,"smali");
        bakSmali(dex1, smali);
        File cocos2dxActivity=new File(smali,"org\\cocos2dx\\lib\\Cocos2dxActivity.smali");
        String code=FileUtils.readFileToString(cocos2dxActivity,"UTF-8");
        code=code.replace("invoke-static {v3}, Ljava/lang/System;->loadLibrary(Ljava/lang/String;)V",
                "invoke-static {v3}, Ljava/lang/System;->loadLibrary(Ljava/lang/String;)V\n" +
                "invoke-static {}, Lcom/bigzhao/jianrmagicbox/FakeCode;->loadNative()V");
        FileUtils.writeStringToFile(cocos2dxActivity, code);
        bakSmali(dex2, smali);
        if (outputDex.exists()) FileUtils.deleteQuietly(outputDex);
        File moduleSmali=new File(smali,"../moduleSmali");
        FileUtils.moveDirectory(new File(smali,"com/bigzhao/jianrmagicbox/module"),new File(moduleSmali,"com/bigzhao/jianrmagicbox/module"));
        smali(moduleSmali,moduleDex);
        //FileUtils.copyFile(new File("E:\\IntellijWS\\DynamicApkLoaderBuilder\\tools\\classes.dex"),moduleDex);
        smali(smali, outputDex);
    }

    private String processManifest(String xml) {
        xml=xml.replaceAll("package=\"[\\w\\.]+\"","package=\"com.bigzhao.jianrmagicbox\"");
        xml=xml.replaceAll("<intent-filter[\\s\\S]*?android.intent.category.LAUNCHER[\\s\\S]*?</intent-filter>","");
        xml=xml.replaceAll("(?=\\s*</application>)",
                "<activity android:theme=\"@android:01030007\"\n" +
                "android:label=\"战舰少女R·改\"\n" +
                "android:name=\"com.bigzhao.jianrmagicbox.LoaderActivity\"\n" +
                "			android:screenOrientation=\"6\"\n" +
                "			android:configChanges=\"0x000004A0\"\n" +
                "			>\n" +
                "			 android:exported=\"true\"\n" +
                "			<intent-filter\n" +
                "				>\n" +
                "				<action\n" +
                "					android:name=\"android.intent.action.MAIN\"\n" +
                "					>\n" +
                "				</action>\n" +
                "				<category\n" +
                "					android:name=\"android.intent.category.LAUNCHER\"\n" +
                "					>\n" +
                "				</category>\n" +
                "			</intent-filter>\n" +
                "		</activity>" +
                "<service android:name=\"com.bigzhao.jianrmagicbox.MagicBoxService\">\n" +
                "            <intent-filter>\n" +
                "                <action android:name=\"com.bigzhao.jianrmagicbox.action.SERVICE\"/>\n" +
                "            </intent-filter>\n" +
                "        </service>\n" +
                "        <receiver android:name=\".MagicBoxReciever\">\n" +
                "            <intent-filter>\n" +
                "                <action android:name=\"com.bigzhao.jianrmagicbox.action.RECEIVER\"/>\n" +
                "            </intent-filter>\n" +
                "        </receiver>" +
                        "");
        return xml;
    }


    private Document buildXmlDom(String xml) throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        return builder.parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));
    }
}
