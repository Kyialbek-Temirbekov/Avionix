package avia.cloud.discovery.util;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.util.Base64;

public class ImageUtils {
    public static String compressAndGetBase64Image(byte[] image) throws IOException {
        if(image == null) {
            return null;
        }
        System.out.println("Initial " + image.length);
        return getBase64Image(compressImage(image));
    }
    public static String getBase64Image(byte[] image) throws IOException {
        if(image == null) {
            return null;
        }
        String mimeType = URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(image));
        String base64Image = Base64.getEncoder().encodeToString(image);
        return "data:" + mimeType + ";base64," + base64Image;
    }
    public static byte[] compressImage(byte[] image) throws IOException {
        return compressImage(image, 0.4f);
    }
    public static byte[] compressImage(byte[] image, float quality) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(image);
        BufferedImage bufferedImage = ImageIO.read(inputStream);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        String mimeType = URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(image));
        ImageWriter imageWriter = ImageIO.getImageWritersByFormatName(mimeType.substring(6)).next();

        ImageWriteParam imageWriteParam = imageWriter.getDefaultWriteParam();
        imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        imageWriteParam.setCompressionQuality(quality); // Quality varies from 0.0 to 1.0

        try (ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(outputStream)) {
            imageWriter.setOutput(imageOutputStream);
            imageWriter.write(null, new javax.imageio.IIOImage(bufferedImage, null, null), imageWriteParam);
        }

        imageWriter.dispose();
        outputStream.close();
        inputStream.close();

        return outputStream.toByteArray();
    }
}
