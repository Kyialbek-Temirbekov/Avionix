package avia.cloud.client.util;

import lombok.SneakyThrows;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.util.Base64;

public class ImageUtils {
    @SneakyThrows
    public static String getBase64Image(byte[] image) {
        if(image == null) {
            return null;
        }
        String mimeType = URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(image));
        String base64Image = Base64.getEncoder().encodeToString(image);
        return "data:" + mimeType + ";base64," + base64Image;
    }
}
