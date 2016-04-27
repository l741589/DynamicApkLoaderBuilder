package com.bigzhao.apk;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by Roy on 16-4-27.
 */
public class ZipUtils {

    private static void zip(ZipOutputStream out,String dir,File f) throws IOException {
        if (f.isDirectory()){
            File[] fs=f.listFiles();
            String newDir=dir + f.getName() + "/";
            if (fs!=null) {
                for (File e:fs) zip(out, newDir, e);
            }
        }else{
            ZipEntry entry=new ZipEntry(dir+f.getName());
            out.putNextEntry(entry);
            out.write(FileUtils.readFileToByteArray(f));
        }
    }

    public static void zip(File in,File out) throws IOException {
        try(ZipOutputStream ouf=new ZipOutputStream(new FileOutputStream(out), Charset.forName("UTF-8"))){;
            if (in.isDirectory()){
                File[] fs=in.listFiles();
                if (fs!=null) for (File e:fs) zip(ouf, "", e);
            }
        }
    }

    public static void unzip(File in,File out) throws IOException{
        try(ZipInputStream is=new ZipInputStream(new FileInputStream(in),Charset.forName("UTF-8"))){
            for (ZipEntry e=is.getNextEntry();e!=null;e=is.getNextEntry()){
                File f=new File(out,e.getName());
                if (!f.getParentFile().exists()) f.getParentFile().mkdirs();
                byte[] bs=IOUtils.toByteArray(is);
                FileUtils.writeByteArrayToFile(f,bs);
            }
        }
    }
}
