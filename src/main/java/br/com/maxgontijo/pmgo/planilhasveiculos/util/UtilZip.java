package br.com.maxgontijo.pmgo.planilhasveiculos.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UtilZip {
    public static void unzipOneSingleFile(InputStream in, OutputStream out) {
        byte[] buffer = new byte[1024];
        try {
            ZipInputStream zis = new ZipInputStream(in);
            ZipEntry ze = zis.getNextEntry();
            while (ze != null) {
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    out.write(buffer, 0, len);
                }
                out.close();
                zis.closeEntry();
                ze = zis.getNextEntry();
            }
            zis.closeEntry();
            zis.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}