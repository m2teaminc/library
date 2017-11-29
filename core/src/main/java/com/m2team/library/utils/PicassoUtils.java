package com.m2team.library.utils;/*
package com.m2team.library.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.widget.ImageView;

import com.m2team.library.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

*/
/**
 * @Description:????:Picasso???????
 * @Prject: CommonUtilLibrary
 * @Package: com.jingewenku.abrahamcaijin.commonutil
 * @author: AbrahamCaiJin
 * @date: 2017?05?19? 15:47
 * @Copyright: ??????
 * @Company:
 * @version: 1.0.0
 *//*


*/
/**
 * picasso ???????<BR/>
 * ???????????????????????ImageView???????????????????
 * ??4.0+????HTTP???????????????????????????
 * <BR/> Picasso ???okhttp??
 *
 * ????Picasso??????okhttp?????????????????????
 Picasso detected an unsupported OkHttp on the classpath
 ??????????????compile 'com.squareup.okhttp:okhttp-urlconnection:2.2.0'
 ???????????
 compile 'com.squareup.okhttp:okhttp:2.4.0'
 compile 'com.squareup.okhttp:okhttp-urlconnection:2.2.0'
 compile 'com.squareup.picasso:picasso:2.4.0'

 *//*

public class PicassoUtils {
    private static PicassoUtils instance;
    */
/**??*//*

    public static String PICASSO_BITMAP_SHOW_CIRCLE_TYPE="PicassoUtils_Circle_Type";
    */
/**??*//*

    public static String PICASSO_BITMAP_SHOW_ROUND_TYPE="PicassoUtils_Round_Type";
    */
/**??*//*

    public static String PICASSO_BITMAP_SHOW_NORMAL_TYPE="PicassoUtils_Normal_Type";
    public static PicassoUtils getinstance(){
        if(instance==null){
            synchronized (PicassoUtils.class) {
                if(instance==null){
                    instance=new PicassoUtils();
                }
            }
        }
        return instance;
    }
    //Picasso????????
    //Picasso.with(context).load("http://i.imgur.com/DvpvklR.png").into(imageView);
    //Picasso.with(context).load(url).into(view);
    //Picasso.with(context).load(url) .resize(50, 50).centerCrop().into(imageView)
    ////???placeholder?resource????getResource.getDrawable????????????????color id
    //Picasso.with(context).load(url).placeholder(R.drawable.user_placeholder).error(R.drawable.user_placeholder_error).into(imageView);
    //
    // Resources, assets, files, content providers ???????
    //Picasso.with(context).load(R.drawable.landing_screen).into(imageView1);
    //Picasso.with(context).load("file:///android_asset/DvpvklR.png").into(imageView2);
    //Picasso.with(context).load(new File(...)).into(imageView3);
    ////????notification???
    //Picasso.with(activity).load(Data.URLS[new Random().nextInt(Data.URLS.length)]).resizeDimen(R.dimen.notification_icon_width_height,    R.dimen.notification_icon_width_height).into(remoteViews, R.id.photo, NOTIFICATION_ID, notification);
    ////???????tag???????????context??????????context tag?pause?resume???
    //Picasso.with(context).load(url).placeholder(R.drawable.placeholder).error(R.drawable.error).fit().tag(context).into(view);
    ////??onScrollStateChanged???????
    //picasso.resumeTag(context);
    //picasso.pauseTag(context);
    //Picasso.with(context).load(contactUri).placeholder(R.drawable.contact_picture_placeholder).tag(context).into(holder.icon);
    */
/**
     * ????????
     * @param context
     * @param path
     *  <BR/>
     *  String imagePath = "/mnt/sdcard/phone_pregnancy/header.png";  <BR/>
        String imagefileUrl = Scheme.FILE.wrap(imagePath); <BR/>
        //?????Content provider
        String contentprividerUrl = "content://media/external/audio/albumart/13";   <BR/>
        //?????assets
        //  String assetsUrl = Scheme.ASSETS.wrap("image.png");  <BR/>
        String assetsUrl = "assets://fail_image.9.png";  <BR/>
        //?????  drawable
        //  String drawableUrl = Scheme.DRAWABLE.wrap("R.drawable.ic_launcher.png");<BR/>
        String drawableUrl = "drawable://" + R.drawable.ic_add; <BR/>
        //?????  ??
        String neturi = "http://ww2.sinaimg.cn/large/49aaa343jw1dgwd0qvb4pj.jpg";<BR/>
        <P>
     * @param imageView
     * @param placeholderimage  ????
     * @param errorimage  ??????
     * @param bitmapShowType   PICASSO_BITMAP_SHOW_CIRCLE_TYPE ? PICASSO_BITMAP_SHOW_ROUND_TYPE ?PICASSO_BITMAP_SHOW_NORMAL_TYPE
     * @param roundRadius  ??????
     *//*

    public void LoadImage(Context context,String path,ImageView imageView,int placeholderimage,int errorimage,String bitmapShowType,float roundRadius){
        if(bitmapShowType.equals(PICASSO_BITMAP_SHOW_CIRCLE_TYPE)){
            Picasso.with(context).load(path).placeholder(placeholderimage).error(errorimage).transform(new CircleTransform()).into(imageView);
        }else if(bitmapShowType.equals(PICASSO_BITMAP_SHOW_ROUND_TYPE)){
            Picasso.with(context).load(path).placeholder(placeholderimage).error(errorimage).transform(new RoundTransform(roundRadius)).into(imageView);
        }else {
            Picasso.with(context).load(path).placeholder(placeholderimage).error(errorimage).into(imageView);
        }
    }
    */
/**
     * ?????? ??id
     * @param context
     * @param localimage  R.drawable.landing_screen
     * @param imageView
     * @param bitmapShowType   PICASSO_BITMAP_SHOW_CIRCLE_TYPE ? PICASSO_BITMAP_SHOW_ROUND_TYPE ?PICASSO_BITMAP_SHOW_NORMAL_TYPE
     * @param roundRadius  ??????
     *//*

    public void LoadImage(Context context,int localimage,ImageView imageView,String bitmapShowType,float roundRadius){
        if(bitmapShowType.equals(PICASSO_BITMAP_SHOW_CIRCLE_TYPE)){
            Picasso.with(context).load(localimage).placeholder(R.drawable.img_loading).error(R.drawable.img_load_error).transform(new CircleTransform()).into(imageView);
        }else if(bitmapShowType.equals(PICASSO_BITMAP_SHOW_ROUND_TYPE)){
            Picasso.with(context).load(localimage).transform(new RoundTransform(roundRadius)).into(imageView);
        }else {
            Picasso.with(context).load(localimage).into(imageView);
        }
    }
    */
/**
     * ???? ????  ?????? ?centerCrop() ?
     * @param context
     * @param path
     * @param imageView
     * @param targetWidth
     * @param targetHeight
     * @param bitmapShowType   PICASSO_BITMAP_SHOW_CIRCLE_TYPE ? PICASSO_BITMAP_SHOW_ROUND_TYPE ?PICASSO_BITMAP_SHOW_NORMAL_TYPE
     * @param roundRadius  ??????
     *//*

    public void LoadImageWithWidtAndHeight(Context context,String path,ImageView imageView,int targetWidth,int targetHeight,String bitmapShowType,float roundRadius){
        if(bitmapShowType.equals(PICASSO_BITMAP_SHOW_CIRCLE_TYPE)){
            Picasso.with(context).load(path).resize(targetWidth, targetHeight).centerCrop().transform(new CircleTransform()).into(imageView);
        }else if(bitmapShowType.equals(PICASSO_BITMAP_SHOW_ROUND_TYPE)){
            Picasso.with(context).load(path).resize(targetWidth, targetHeight).centerCrop().transform(new RoundTransform(roundRadius)).into(imageView);
        }else {
            Picasso.with(context).load(path).resize(targetWidth, targetHeight).centerCrop().into(imageView);
        }
    }
    //--------------------------------------------------
    */
/**
     *??????
     *//*

    public class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);
            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }
    //------------------------------------------------------
    */
/**
     * ????
     *//*

    public class RoundTransform implements Transformation{
        private float radius;
        public RoundTransform(float radius) {
            this.radius=radius;
        }
        @Override
        public String key() {
            return "round";
        }

        @Override
        public Bitmap transform(Bitmap bitmap) {
            int size = Math.min(bitmap.getWidth(), bitmap.getHeight());

            int x = (bitmap.getWidth() - size) / 2;
            int y = (bitmap.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(bitmap, x, y, size, size);
            if (squaredBitmap != bitmap) {
                bitmap.recycle();
            }
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            final RectF rectF = new RectF(rect);

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, radius, radius, paint);

            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);
            squaredBitmap.recycle();
            return output;
        }

    }
}
*/
