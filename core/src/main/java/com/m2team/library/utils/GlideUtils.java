package com.m2team.library.utils;/*
package com.m2team.library.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;


public class GlideUtils {
    private static GlideUtils instance;
    public static GlideUtils getInstance(){
        if(instance==null){
            synchronized (GlideUtils.class) {
                if(instance==null){
                    instance=new GlideUtils();
                }
            }
        }
        return instance;
    }


    public static String LOAD_BITMAP="GLIDEUTILS_GLIDE_LOAD_BITMAP";


    public static String LOAD_GIF="GLIDEUTILS_GLIDE_LOAD_GIF";


    public void LoadContextBitmap(Context context,String path,ImageView imageView,int placeid,int errorid,String bitmapOrgif){
        if(bitmapOrgif==null||bitmapOrgif.equals(LOAD_BITMAP)){
            Glide.with(context).load(path).placeholder(placeid).error(errorid).crossFade().into(imageView);
        }else if(bitmapOrgif.equals(LOAD_GIF)){
            Glide.with(context).load(path).asGif().crossFade().into(imageView);
        }
    }

*
     * Glide????????Fragment ???????
     * @param fragment
     * @param path
     * @param imageView
     * @param placeid
     * @param errorid
     * @param bitmapOrgif  ?????? ??GIF?? ?GIF????bitmap?????


    public void LoadFragmentBitmap(android.app.Fragment fragment,String path,ImageView imageView,int placeid,int errorid,String bitmapOrgif){
        if(bitmapOrgif==null||bitmapOrgif.equals(LOAD_BITMAP)){
            Glide.with(fragment).load(path).placeholder(placeid).error(errorid).crossFade().into(imageView);
        }else if(bitmapOrgif.equals(LOAD_GIF)){
            Glide.with(fragment).load(path).asGif().crossFade().into(imageView);
        }
    }
*
     * Glide????????support.v4.app.Fragment???????
     * @param fragment
     * @param path
     * @param imageView
     * @param placeid
     * @param errorid
     * @param bitmapOrgif  ?????? ??GIF?? ?GIF????bitmap?????


    public void LoadSupportv4FragmentBitmap(android.support.v4.app.Fragment fragment,String path,ImageView imageView,int placeid,int errorid,String bitmapOrgif){
        if(bitmapOrgif==null||bitmapOrgif.equals(LOAD_BITMAP)){
            Glide.with(fragment).load(path).placeholder(placeid).error(errorid).crossFade().into(imageView);
        }else if(bitmapOrgif.equals(LOAD_GIF)){
            Glide.with(fragment).load(path).asGif().crossFade().into(imageView);
        }
    }
    //---------------------????-------------------
*
     * ????????
     * ??Application????Glide?????Activity/Fragment??????
     * <BR/>??activity ???Activity??????
     * <BR/>??FragmentActivity ???FragmentActivity??????
     * @param context
     * @param path
     * @param imageView


    @SuppressWarnings("unchecked")
    public void LoadContextCircleBitmap(Context context,String path,ImageView imageView){
        Glide.with(context).load(path).bitmapTransform(new GlideCircleTransform(context)).into(imageView);
    }
*
     * Glide????????????android.app.Fragment??????
     * @param fragment
     * @param path
     * @param imageView


    @SuppressWarnings("unchecked")
    public void LoadfragmentCircleBitmap(android.app.Fragment fragment,String path,ImageView imageView){
        Glide.with(fragment).load(path).bitmapTransform(new GlideCircleTransform(fragment.getActivity())).into(imageView);
    }
*
     * Glide????????????android.support.v4.app.Fragment??????
     * @param fragment
     * @param path
     * @param imageView


    @SuppressWarnings("unchecked")
    public void LoadSupportv4FragmentCircleBitmap(android.support.v4.app.Fragment fragment,String path,ImageView imageView){
        Glide.with(fragment).load(path).bitmapTransform(new GlideCircleTransform(fragment.getActivity())).into(imageView);
    }
    //-----------------------????----------------------
*
     * ????????
     * ??Application????Glide?????Activity/Fragment??????
     * <BR/>??activity ???Activity??????
     * <BR/>??FragmentActivity ???FragmentActivity??????
     * @param context
     * @param path
     * @param imageView
     * @param roundradius ?????>0?


    @SuppressWarnings("unchecked")
    public void LoadContextRoundBitmap(Context context,String path,ImageView imageView,int roundradius){
        if(roundradius<0){
            Glide.with(context).load(path).bitmapTransform(new GlideRoundTransform(context)).into(imageView);
        }else{
            Glide.with(context).load(path).bitmapTransform(new GlideRoundTransform(context,roundradius)).into(imageView);
        }
    }
*
     * Glide????????????android.app.Fragment??????
     * @param fragment
     * @param path
     * @param imageView
     * @param roundradius


    @SuppressWarnings("unchecked")
    public void LoadfragmentRoundBitmap(android.app.Fragment fragment,String path,ImageView imageView,int roundradius){
        if(roundradius<0){
            Glide.with(fragment).load(path).bitmapTransform(new GlideRoundTransform(fragment.getActivity())).into(imageView);
        }else{
            Glide.with(fragment).load(path).bitmapTransform(new GlideRoundTransform(fragment.getActivity(),roundradius)).into(imageView);
        }
    }
*
     * Glide????????????android.support.v4.app.Fragment??????
     * @param fragment
     * @param path
     * @param imageView
     * @param roundradius


    @SuppressWarnings("unchecked")
    public void LoadSupportv4FragmentRoundBitmap(android.support.v4.app.Fragment fragment,String path,ImageView imageView,int roundradius){
        if(roundradius<0){
            Glide.with(fragment).load(path).bitmapTransform(new GlideRoundTransform(fragment.getActivity())).into(imageView);
        }else{
            Glide.with(fragment).load(path).bitmapTransform(new GlideRoundTransform(fragment.getActivity(),roundradius)).into(imageView);
        }
    }
    //-------------------------------------------------
*
     * Glide ??????
     * ??Application????Glide?????Activity/Fragment??????
     * <BR/>??activity ???Activity??????
     * <BR/>??FragmentActivity ???FragmentActivity??????
     * @param context
     * @param path
     * @param imageView


    @SuppressWarnings("unchecked")
    public void LoadContextBlurBitmap(Context context,String path,ImageView imageView){
        Glide.with(context).load(path).bitmapTransform(new BlurTransformation(context)).into(imageView);
    }
*
     * Glide ?????? ???Fragment??????
     * @param fragment
     * @param path
     * @param imageView


    @SuppressWarnings("unchecked")
    public void LoadFragmentBlurBitmap(android.app.Fragment fragment,String path,ImageView imageView){
        Glide.with(fragment).load(path).bitmapTransform(new BlurTransformation(fragment.getActivity())).into(imageView);
    }
*
     * Glide ?????? ???support.v4.app.Fragment??????
     * @param fragment
     * @param path
     * @param imageView


    @SuppressWarnings("unchecked")
    public void LoadSupportv4FragmentBlurBitmap(android.support.v4.app.Fragment fragment,String path,ImageView imageView){
        Glide.with(fragment).load(path).bitmapTransform(new BlurTransformation(fragment.getActivity())).into(imageView);
    }
    //---------------------------------------------------------
*
     * ????
     *??Application????Glide?????Activity/Fragment??????
     * <BR/>??activity ???Activity??????
     * <BR/>??FragmentActivity ???FragmentActivity??????
     * @param context
     * @param path
     * @param imageView
     * @param rotateRotationAngle ????


    @SuppressWarnings("unchecked")
    public void LoadContextRotateBitmap(Context context,String path,ImageView imageView,Float rotateRotationAngle){
        Glide.with(context).load(path).bitmapTransform(new RotateTransformation(context, rotateRotationAngle)).into(imageView);
    }
*
     * Glide ?????? ???Fragment??????
     * @param fragment
     * @param path
     * @param imageView
     * @param rotateRotationAngle


    @SuppressWarnings("unchecked")
    public void LoadFragmentRotateBitmap(android.app.Fragment fragment,String path,ImageView imageView,Float rotateRotationAngle){
        Glide.with(fragment).load(path).bitmapTransform(new RotateTransformation(fragment.getActivity(), rotateRotationAngle)).into(imageView);
    }
*
     * Glide ?????? ???support.v4.app.Fragment??????
     * @param fragment
     * @param path
     * @param imageView
     * @param rotateRotationAngle


    @SuppressWarnings("unchecked")
    public void LoadSupportv4FragmentRotateBitmap(android.support.v4.app.Fragment fragment,String path,ImageView imageView,Float rotateRotationAngle){
        Glide.with(fragment).load(path).bitmapTransform(new RotateTransformation(fragment.getActivity(), rotateRotationAngle)).into(imageView);
    }
    //----------------------??---------------------------
*
     *??


    public class RotateTransformation extends BitmapTransformation {

        private float rotateRotationAngle = 0f;

        public RotateTransformation(Context context, float rotateRotationAngle) {
            super( context );

            this.rotateRotationAngle = rotateRotationAngle;
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            Matrix matrix = new Matrix();

            matrix.postRotate(rotateRotationAngle);

            return Bitmap.createBitmap(toTransform, 0, 0, toTransform.getWidth(), toTransform.getHeight(), matrix, true);
        }

        @Override
        public String getId() {
            return "rotate" + rotateRotationAngle;
        }
    }
    //--------------------------------------------------
*
     *?????


    public  class GlideCircleTransform extends BitmapTransformation {
        public GlideCircleTransform(Context context) {
            super(context);
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return circleCrop(pool, toTransform);
        }

        private  Bitmap circleCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;

            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

            Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_4444);
            if (result == null) {
                result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_4444);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);
            return result;
        }
        @Override
        public String getId() {
            return getClass().getName();
        }
    }
    //-----------------------------????----------------------------------
*
     *????


    public class BlurTransformation extends BitmapTransformation {

        private RenderScript rs;

        public BlurTransformation(Context context) {
            super( context );

            rs = RenderScript.create( context );
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            Bitmap blurredBitmap = toTransform.copy( Bitmap.Config.ARGB_8888, true );

            // Allocate memory for Renderscript to work with
            Allocation input = Allocation.createFromBitmap(
                rs,
                blurredBitmap,
                Allocation.MipmapControl.MIPMAP_FULL,
                Allocation.USAGE_SHARED
            );
            Allocation output = Allocation.createTyped(rs, input.getType());

            // Load up an instance of the specific script that we want to use.
            ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            script.setInput(input);

            // Set the blur radius
            script.setRadius(10);

            // Start the ScriptIntrinisicBlur
            script.forEach(output);

            // Copy the output to the blurred bitmap
            output.copyTo(blurredBitmap);

            toTransform.recycle();

            return blurredBitmap;
        }

        @Override
        public String getId() {
            return "blur";
        }
    }
    //-------------------????????------------------------------
*
     *????????


    public  class GlideRoundTransform extends BitmapTransformation {

        private  float radius = 0f;

        public GlideRoundTransform(Context context) {
            this(context, 4);
        }

*
         * ???????
         * @param context
         * @param dp


        public GlideRoundTransform(Context context, int dp) {
            super(context);
            this.radius = Resources.getSystem().getDisplayMetrics().density * dp;
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return roundCrop(pool, toTransform);
        }

        private  Bitmap roundCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;

            Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
            canvas.drawRoundRect(rectF, radius, radius, paint);
            return result;
        }

        @Override public String getId() {
            return getClass().getName() + Math.round(radius);
        }
    }
}
*/
