package com.google.zxing.client.android;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.GlobalHistogramBinarizer;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.GenericMultipleBarcodeReader;
import com.google.zxing.multi.MultipleBarcodeReader;
import com.google.zxing.qrcode.QRCodeReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

public class DecodeBitmapUtils {
    public static final Map<DecodeHintType, Object> HINTS = new EnumMap<>(DecodeHintType.class);

    static {
        List<BarcodeFormat> allFormats = new ArrayList<>();
        allFormats.add(BarcodeFormat.AZTEC);
        allFormats.add(BarcodeFormat.CODABAR);
        allFormats.add(BarcodeFormat.CODE_39);
        allFormats.add(BarcodeFormat.CODE_93);
        allFormats.add(BarcodeFormat.CODE_128);
        allFormats.add(BarcodeFormat.DATA_MATRIX);
        allFormats.add(BarcodeFormat.EAN_8);
        allFormats.add(BarcodeFormat.EAN_13);
        allFormats.add(BarcodeFormat.ITF);
        allFormats.add(BarcodeFormat.MAXICODE);
        allFormats.add(BarcodeFormat.PDF_417);
        allFormats.add(BarcodeFormat.QR_CODE);
        allFormats.add(BarcodeFormat.RSS_14);
        allFormats.add(BarcodeFormat.RSS_EXPANDED);
        allFormats.add(BarcodeFormat.UPC_A);
        allFormats.add(BarcodeFormat.UPC_E);
        allFormats.add(BarcodeFormat.UPC_EAN_EXTENSION);
        HINTS.put(DecodeHintType.TRY_HARDER, BarcodeFormat.QR_CODE);
        HINTS.put(DecodeHintType.POSSIBLE_FORMATS, allFormats);
        HINTS.put(DecodeHintType.CHARACTER_SET, "utf-8");
    }
    /**
     * 处理二维码图片
     */
    public static Result decodeQRBitmap(Bitmap bitmap) {

        Result result = decode(bitmap);

        if (result == null) {
            result = decode(operateBitmap(bitmap));
        }
//        if (result == null) {
//            result = processImage(bitmap);
//        }
        if (result != null) {
            Log.e("result", result.getText());
        }

        return result;
    }

    //1，缩小图片2，将图片添加一个背景
    private static Bitmap operateBitmap(Bitmap bitmap) {

        Bitmap bitmap2 = Bitmap.createBitmap(512, 512, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap2);
        canvas.drawColor(Color.parseColor("#eef3fa"));

        int baseWidth = 400;

        if (bitmap.getWidth() > baseWidth || bitmap.getHeight() > baseWidth) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();

            if (width > height) {
                height = baseWidth * height / width;
                width = baseWidth;
            } else {
                width = baseWidth * width / height;
                height = baseWidth;
            }

            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
        }

        int left = 256 - bitmap.getWidth() / 2;
        int top = 256 - bitmap.getHeight() / 2;
        canvas.drawBitmap(bitmap, left, top, new Paint());

        return bitmap2;
    }

    private static Result decode(Bitmap bitmap) {

        Result result;
        RGBLuminanceSource source = null;
        try {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int[] pixels = new int[width * height];
            bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
            source = new RGBLuminanceSource(width, height, pixels);
            result = new MultiFormatReader().decode(new BinaryBitmap(new HybridBinarizer(source)), HINTS);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            if (source != null) {
                try {
                    result = new MultiFormatReader().decode(new BinaryBitmap(new GlobalHistogramBinarizer(source)), HINTS);
                    return result;
                } catch (Throwable e2) {
                    e2.printStackTrace();
                }
            }
            return null;
        }
    }


    private static Result processImage(Bitmap image) {

        int width = image.getWidth();
        int height = image.getHeight();
        int[] data = new int[width * height];
        image.getPixels(data, 0, width, 0, 0, width, height);
        RGBLuminanceSource source = new RGBLuminanceSource(width, height, data);

        //LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new GlobalHistogramBinarizer(source));
        Collection<Result> results = new ArrayList<>(1);

        try {
            Reader reader = new MultiFormatReader();
            try {
                // Look for multiple barcodes
                MultipleBarcodeReader multiReader = new GenericMultipleBarcodeReader(reader);
                Result[] theResults = multiReader.decodeMultiple(bitmap, HINTS);
                if (theResults != null) {
                    results.addAll(Arrays.asList(theResults));
                }
            } catch (ReaderException re) {
            }

//            if (results.isEmpty()) {
//                try {
//                    // Look for pure barcode
//                    Result theResult = reader.decode(bitmap, HINTS_PURE);
//                    if (theResult != null) {
//                        results.add(theResult);
//                    }
//                } catch (ReaderException re) {
//                }
//            }

            if (results.isEmpty()) {
                try {
                    // Look for normal barcode in photo
                    Result theResult = reader.decode(bitmap, HINTS);
                    if (theResult != null) {
                        results.add(theResult);
                    }
                } catch (ReaderException re) {
                }
            }

        } catch (RuntimeException re) {
            // Call out unexpected errors in the log clearly
            //log.log(Level.WARNING, "Unexpected exception from library", re);

        }

        if (results.size() > 0) {
            return results.iterator().next();
        } else {
            return null;
        }
    }

}