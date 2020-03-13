package br.com.maxgontijo.pmgo.planilhasveiculos.util;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

public class CharsetDetector {
    public static boolean isUTF8(byte[] bytes) {
        try {
            Charset charsetUtf = Charset.forName("UTF-8");
            CharsetDecoder decoder = charsetUtf.newDecoder();
            decoder.reset();
            decoder.decode(ByteBuffer.wrap(bytes));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Charset getCharset(byte[] bytes) {
        try {
            if (isUTF8(bytes)) {
                return Charset.forName("UTF-8");
            } else {
                try {
                    return Charset.forName("ISO-8859-15");
                } catch (Exception e) {
                    return Charset.forName("ISO-8859-1");
                }
            }
        } catch (Exception e) {
            return Charset.defaultCharset();
        }
    }

    public static InputStreamReader createTextInputStreamReader(InputStream in) throws IOException {
        byte[] targetArray;
        targetArray = IOUtils.toByteArray(in);
        Charset charset = CharsetDetector.getCharset(targetArray);
        in = new ByteArrayInputStream(targetArray);
        return new InputStreamReader(in, charset);
    }
}
