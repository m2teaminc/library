package com.m2team.library.utils;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import timber.log.Timber;

import static android.graphics.Bitmap.Config.ARGB_8888;
import static android.graphics.Color.WHITE;
import static android.graphics.PorterDuff.Mode.DST_IN;

public final class ImageUtils {

    private ImageUtils() {
        //never called
    }

    public static Bitmap getBitmap(final String imagePath) {
        return getBitmap(imagePath, 1);
    }

    public static Bitmap getBitmap(final String imagePath, final int sampleSize) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inDither = false;
        options.inSampleSize = sampleSize;

        RandomAccessFile file = null;
        try {
            file = new RandomAccessFile(imagePath, "r");
            return BitmapFactory.decodeFileDescriptor(file.getFD(), null,
                    options);
        } catch (IOException e) {
            Timber.d(e, "Could not get cached bitmap.");
            return null;
        } finally {
            if (file != null)
                try {
                    file.close();
                } catch (IOException e) {
                    Timber.d(e, "Could not get cached bitmap.");
                }
        }
    }

    public static Point getSize(final String imagePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        RandomAccessFile file = null;
        try {
            file = new RandomAccessFile(imagePath, "r");
            BitmapFactory.decodeFileDescriptor(file.getFD(), null, options);
            return new Point(options.outWidth, options.outHeight);
        } catch (final IOException e) {
            Timber.d(e, "Could not get size.");
            return null;
        } finally {
            if (file != null) {
                try {
                    file.close();
                } catch (final IOException e) {
                    Timber.d(e, "Could not get size.");
                }
            }
        }
    }

    public static Bitmap getBitmap(final String imagePath, final int width, final int height) {
        final Point size = getSize(imagePath);
        int currWidth = size.x;
        int currHeight = size.y;

        int scale = 1;
        while (currWidth >= width || currHeight >= height) {
            currWidth /= 2;
            currHeight /= 2;
            scale *= 2;
        }

        return getBitmap(imagePath, scale);
    }

    public static Bitmap getBitmap(final File image, final int width, final int height) {
        return getBitmap(image.getAbsolutePath(), width, height);
    }

    public static Bitmap getBitmap(final File image) {
        return getBitmap(image.getAbsolutePath());
    }

    public static void setImage(final String imagePath, final ImageView view) {
        setImage(new File(imagePath), view);
    }

    public static void setImage(final File image, final ImageView view) {
        final Bitmap bitmap = getBitmap(image);
        if (bitmap != null) {
            view.setImageBitmap(bitmap);
        }
    }

    public static Bitmap roundCorners(final Bitmap source, final float radius) {
        final int width = source.getWidth();
        final int height = source.getHeight();

        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(WHITE);

        final Bitmap clipped = Bitmap.createBitmap(width, height, ARGB_8888);
        Canvas canvas = new Canvas(clipped);
        canvas.drawRoundRect(new RectF(0, 0, width, height), radius, radius,
                paint);
        paint.setXfermode(new PorterDuffXfermode(DST_IN));

        final Bitmap rounded = Bitmap.createBitmap(width, height, ARGB_8888);
        canvas = new Canvas(rounded);
        canvas.drawBitmap(source, 0, 0, null);
        canvas.drawBitmap(clipped, 0, 0, paint);

        source.recycle();
        clipped.recycle();

        return rounded;
    }
}

