package com.bigzhao.apk;

import android.content.Context;
import com.bigzhao.xml2axml.Encoder;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SystemUtils;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws Exception {
        Merger merger=new Merger(args.length>=3?new File(args[2]): SystemUtils.getUserDir());
        merger.merge(new File(args[0]), new File(args[1]));
    }
}
