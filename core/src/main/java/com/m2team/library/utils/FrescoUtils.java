package com.m2team.library.utils;/*
package com.m2team.library.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.internal.Supplier;
import com.facebook.common.util.ByteConstants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.common.ImageDecodeOptions;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequest.RequestLevel;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.m2team.library.R;

*/
/**
 * @Description:????:Fresco???????
 * @Prject: CommonUtilLibrary
 * @Package: com.jingewenku.abrahamcaijin.commonutil
 * @author: AbrahamCaiJin
 * @date: 2017?05?19? 14:38
 * @Copyright: ??????
 * @Company:
 * @version: 1.0.0
 *//*


public class FrescoUtils {

    private static final int MAX_HEAP_SIZE = (int) Runtime.getRuntime().maxMemory();//???????
    public static final int MAX_MEMORY_CACHE_SIZE = MAX_HEAP_SIZE / 4;//???????

    public static final int MAX_SMALL_DISK_VERYLOW_CACHE_SIZE = 5 * ByteConstants.MB;//??????????????????????????????????????????????????????????
    public static final int MAX_SMALL_DISK_LOW_CACHE_SIZE 	= 10 * ByteConstants.MB;//?????????????????????????????????????????????????????????
    public static final int MAX_SMALL_DISK_CACHE_SIZE 		= 20 * ByteConstants.MB;//??????????????????????????????????????????????????????

    public static final int MAX_DISK_CACHE_VERYLOW_SIZE 		= 10 * ByteConstants.MB;//???????????????
    public static final int MAX_DISK_CACHE_LOW_SIZE 			= 30 * ByteConstants.MB;//??????????????
    public static final int MAX_DISK_CACHE_SIZE 				= 50 * ByteConstants.MB;//???????????

    private static final String IMAGE_PIPELINE_SMALL_CACHE_DIR = "imagepipeline_cache";//???????????
    private static final String IMAGE_PIPELINE_CACHE_DIR 		 = "imagepipeline_cache";//????????????

    private static ImagePipelineConfig sImagePipelineConfig;

    private FrescoUtils(){

    }

    */
/**
     *  ????????
     *  FrescoUtils.initConfig(context);
     *
     *  ???????????
     * ? Application ?????????? setContentView() ?????????
     * Fresco.initialize(context);
     *
     * @param context
     * @return
     *//*

    public static void initConfig(Context context) {
        ImagePipelineConfig initImagePipelineConfig = initImagePipelineConfig(context);
        Fresco.initialize(context, initImagePipelineConfig);
    }

    public static ImagePipelineConfig initImagePipelineConfig(Context context) {
        if (sImagePipelineConfig == null) {
            sImagePipelineConfig = configureCaches(context);
        }
        return sImagePipelineConfig;
    }


    // ???????????
    public static Drawable sPlaceholderDrawable;
    public static Drawable sErrorDrawable;

    public static void init(final Resources resources) {
        if (sPlaceholderDrawable == null) {
            sPlaceholderDrawable = resources.getDrawable(R.color.transparency);
        }
        if (sErrorDrawable == null) {
            sErrorDrawable = resources.getDrawable(R.color.gray);
        }
    }

    */
/**
     * ?????
     * @param context
     * @return
     *//*

    private static ImagePipelineConfig configureCaches(Context context) {
        // ????
        final MemoryCacheParams bitmapCacheParams = new MemoryCacheParams(
            FrescoUtils.MAX_MEMORY_CACHE_SIZE, // ?????????????,???????
            Integer.MAX_VALUE, // ?????????????
            FrescoUtils.MAX_MEMORY_CACHE_SIZE, // ????????????????????????,???????
            Integer.MAX_VALUE, // ???????????????????
            Integer.MAX_VALUE); // ???????????????

        // ?????????????????????????
        Supplier<MemoryCacheParams> mSupplierMemoryCacheParams = new Supplier<MemoryCacheParams>() {
            @Override
            public MemoryCacheParams get() {
                return bitmapCacheParams;
            }
        };

        // ????????
        DiskCacheConfig diskSmallCacheConfig = DiskCacheConfig
            .newBuilder(context)
            .setBaseDirectoryPath(context.getApplicationContext().getCacheDir())// ???????
            .setBaseDirectoryName(IMAGE_PIPELINE_SMALL_CACHE_DIR)// ????
            // .setCacheErrorLogger(cacheErrorLogger)//???????????????
            // .setCacheEventListener(cacheEventListener)//????????
            // .setDiskTrimmableRegistry(diskTrimmableRegistry)//??????????????????????
            .setMaxCacheSize(FrescoUtils.MAX_DISK_CACHE_SIZE)// ??????????
            .setMaxCacheSizeOnLowDiskSpace(MAX_SMALL_DISK_LOW_CACHE_SIZE)// ???????,???????????
            .setMaxCacheSizeOnVeryLowDiskSpace(MAX_SMALL_DISK_VERYLOW_CACHE_SIZE)// ???????,?????????
            // .setVersion(version)
            .build();

        // ?????????
        DiskCacheConfig diskCacheConfig = DiskCacheConfig
            .newBuilder(context)
            .setBaseDirectoryPath(Environment.getExternalStorageDirectory().getAbsoluteFile())// ???????
            .setBaseDirectoryName(IMAGE_PIPELINE_CACHE_DIR)// ????
            // .setCacheErrorLogger(cacheErrorLogger)//???????????????
            // .setCacheEventListener(cacheEventListener)//????????
            // .setDiskTrimmableRegistry(diskTrimmableRegistry)//??????????????????????
            .setMaxCacheSize(FrescoUtils.MAX_DISK_CACHE_SIZE)// ??????????
            .setMaxCacheSizeOnLowDiskSpace(MAX_DISK_CACHE_LOW_SIZE)// ???????,???????????
            .setMaxCacheSizeOnVeryLowDiskSpace(MAX_DISK_CACHE_VERYLOW_SIZE)// ???????,?????????
            // .setVersion(version)
            .build();

        // ??????
        ImagePipelineConfig.Builder configBuilder = ImagePipelineConfig
            .newBuilder(context)
            // .setAnimatedImageFactory(AnimatedImageFactory
            // animatedImageFactory)//??????
            .setBitmapMemoryCacheParamsSupplier(mSupplierMemoryCacheParams)// ???????????????????
            // .setCacheKeyFactory(cacheKeyFactory)//??Key??
            // .setEncodedMemoryCacheParamsSupplier(encodedCacheParamsSupplier)//??????????????????????
            // .setExecutorSupplier(executorSupplier)//?????
            // .setImageCacheStatsTracker(imageCacheStatsTracker)//????????
            // .setImageDecoder(ImageDecoder imageDecoder) //???????
            // .setIsPrefetchEnabledSupplier(Supplier<Boolean>
            // isPrefetchEnabledSupplier)//???????????????????????
            .setMainDiskCacheConfig(diskCacheConfig)// ??????????????
            // .setMemoryTrimmableRegistry(memoryTrimmableRegistry)
            // //???????,??????????????????????????????????????????????
            // ?????????????????????
            // .setNetworkFetchProducer(networkFetchProducer)//??????????OkHttp?Volley
            // .setPoolFactory(poolFactory)//???????
            // .setProgressiveJpegConfig(progressiveJpegConfig)//???JPEG?
            // .setRequestListeners(requestListeners)//??????
            // .setResizeAndRotateEnabledForNetwork(boolean
            // resizeAndRotateEnabledForNetwork)//?????????????
            .setSmallImageDiskCacheConfig(diskSmallCacheConfig)// ??????????????????????????
            ;
        return configBuilder.build();
    }


    */
/**
     * ? Application ?????????? setContentView() ?????????
     * Fresco.initialize(context);
     *
     * ?xml?????, ???????
     *
     * !-- ???? -->
     LinearLayout
     xmlns:android="http://schemas.android.com/apk/res/android"
     xmlns:fresco="http://schemas.android.com/apk/res-auto">

     * 	??SimpleDraweeView:
     <com.facebook.drawee.view.SimpleDraweeView
     android:id="@+id/my_image_view"
     android:layout_width="20dp"
     android:layout_height="20dp"
     fresco:placeholderImage="@drawable/my_drawable"
     />
     *
     SimpleDraweeView draweeView = (SimpleDraweeView) findViewById(R.id.my_image_view);
     *
     *
     <com.facebook.drawee.view.SimpleDraweeView
     android:id="@+id/my_image_view"
     android:layout_width="20dp"   // ???wrap_content ????????, ???Java?????setAspectRatio(1.33f);
     android:layout_height="20dp"    // ???wrap_content
     fresco:fadeDuration="300"

     fresco:actualImageScaleType="focusCrop"// ??????. ????focusCrop,?????????????????
     fresco:placeholderImage="@color/wait_color"// ???????????

     fresco:placeholderImageScaleType="fitCenter"
     fresco:failureImage="@drawable/error"// ????????????

     fresco:failureImageScaleType="centerInside"
     fresco:retryImage="@drawable/retrying"// ????,?????????????(???failureImage???)

     fresco:retryImageScaleType="centerCrop"
     fresco:progressBarImage="@drawable/progress_bar"// ????????,???????

     fresco:progressBarImageScaleType="centerInside"
     fresco:progressBarAutoRotateInterval="1000"
     fresco:backgroundImage="@color/blue"
     fresco:overlayImage="@drawable/watermark"
     fresco:pressedStateOverlayImage="@color/red"

     fresco:roundAsCircle="false"// ???????

     fresco:roundedCornerRadius="1dp"// ????,180??????????

     fresco:roundTopLeft="true"
     fresco:roundTopRight="false"
     fresco:roundBottomLeft="false"
     fresco:roundBottomRight="true"
     fresco:roundWithOverlayColor="@color/corner_color"
     fresco:roundingBorderWidth="2dp"
     fresco:roundingBorderColor="@color/border_color"
     />
     *
     *
     * ?????Fresco ??? ?????URI. ???URI??????????????URI?scheme?
     ???
     ??	Scheme	??
     ????	http://, https://	HttpURLConnection ???? ??????????
     ????	file://	FileInputStream
     Content provider	content://	ContentResolver
     asset??????	asset://	AssetManager
     res??????	res://	Resources.openRawResource
     res ??:
     Uri uri = Uri.parse("res://??(??????????????)/" + R.drawable.ic_launcher);
     *
     *//*




    */
/**
     * ??????
     * @param draweeView
     * @param uriString
     *//*

    public static void setImageURI(SimpleDraweeView draweeView , String uriString) {
        Uri uri = Uri.parse(uriString);
        draweeView.setImageURI(uri);
    }

    */
/**
     * ???????
     * @param draweeView ?????
     * @param uriString ??
     * @param width 50
     * @param height 50
     *//*

    public static void changeImgSize(SimpleDraweeView draweeView , String uriString , int width , int height) {
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(uriString))
                                                  .setResizeOptions(new ResizeOptions(width, height))
                                                  .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                                            .setImageRequest(request)
                                            .setAutoPlayAnimations(true)// other setters
                                            .build();
        draweeView.setController(controller);
    }

    */
/**
     * ??gif?
     * @param draweeView
     * @param uriString
     *//*

    public static void loadGif(SimpleDraweeView draweeView , String uriString) {
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(uriString)).build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                                            .setImageRequest(request)
                                            .setAutoPlayAnimations(true)
                                            .build();
        draweeView.setController(controller);
    }

    */
/**
     * ????? ???ImageRequest(????)
     * @param draweeView
     * @param lowResUri
     * @param highResUri
     *//*

    public static void moreImgRequst(SimpleDraweeView draweeView , String lowResUri , String highResUri ) {
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                                            .setLowResImageRequest(ImageRequest.fromUri(Uri.parse(lowResUri)))//????????
                                            .setImageRequest(ImageRequest.fromUri(Uri.parse(highResUri)))//??????
                                            .setOldController(draweeView.getController())
                                            .build();
        draweeView.setController(controller);

    }

    */
/**
     * ?????(???????,???JPEG????)
     * @param draweeView
     * @param uri
     *//*

    public static void localImg(SimpleDraweeView draweeView , Uri uri ) {
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                                                  .setLocalThumbnailPreviewsEnabled(true)
                                                  .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                                            .setImageRequest(request)
                                            .setOldController(draweeView.getController())
                                            .build();
        draweeView.setController(controller);
    }

    */
/**
     * ????
     * @param uri - ????????. ?? ???URIs
     * @param width
     * @param height
     * autoRotateEnabled - ????????.
     * progressiveEnabled - ?????????.
     * postprocessor - ????(postprocess).
     * resizeOptions - ??????????????????
     *//*

    public static void loadImage(SimpleDraweeView draweeView , String uri , int width , int height) {
        ImageRequest request = ImageRequestBuilder
            .newBuilderWithSource(Uri.parse(uri))
            .setAutoRotateEnabled(true)// ????????.
            .setImageDecodeOptions(getImageDecodeOptions())//  ?????
            //.setImageType(ImageType.SMALL)//???????????????????????????????
            //.setLocalThumbnailPreviewsEnabled(true)//??????????????????
            .setLowestPermittedRequestLevel(RequestLevel.FULL_FETCH)//????????  BITMAP_MEMORY_CACHE?ENCODED_MEMORY_CACHE?DISK_CACHE?FULL_FETCH
            .setProgressiveRenderingEnabled(false)//?????????????JPEG??????????????
            .setResizeOptions(new ResizeOptions(width, height))
            .build();

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                                            .setImageRequest(request)
                                            .setOldController(draweeView.getController())
                                            .build();
        draweeView.setController(controller);
    }

    */
/**
     * ????
     * @return
     *//*

    public static ImageDecodeOptions getImageDecodeOptions() {
        ImageDecodeOptions decodeOptions = ImageDecodeOptions.newBuilder()
                                                             // .setBackgroundColor(Color.TRANSPARENT)//???????
                                                             // .setDecodeAllFrames(decodeAllFrames)//?????
                                                             // .setDecodePreviewFrame(decodePreviewFrame)//?????
                                                             // .setForceOldAnimationCode(forceOldAnimationCode)//??????
                                                             // .setFrom(options)//???????????
                                                             // .setMinDecodeIntervalMs(intervalMs)//????????????
                                                             .setUseLastFrameForPreview(true)// ??????????
                                                             .build();
        return decodeOptions;
    }
}
*/
