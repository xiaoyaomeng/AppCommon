package com.panghu.appcommon.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

/**
 * PhBlurUtil
 *
 * @desc Bitmap高斯模糊效果类
 * @autor lijiangping
 * @wechat ljphhj
 * @email lijiangping.zz@gmail.com
 *
 **/
public class PhBlurUtil {

    /**
     * 使bitmap模糊化
     *
     * @param context
     * @param bm
     * @param radius 模糊半径(radius)越大，性能要求越高，模糊半径不能超过25(Supported range 0 < radius <= 25)
     *               ，所以并不能得到模糊度非常高的图片。
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Bitmap fastBlur(Context context, Bitmap bm, int radius) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Bitmap bitmap = bm.copy(bm.getConfig(), true);
            final RenderScript rs = RenderScript.create(context);
            final Allocation input = Allocation.createFromBitmap(rs,
                    bm, Allocation.MipmapControl.MIPMAP_NONE,
                    Allocation.USAGE_SCRIPT);
            final Allocation output = Allocation.createTyped(rs,
                    input.getType());
            ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs,
                    Element.U8_4(rs));
            script.setRadius(radius);
            script.setInput(input);
            script.forEach(output);
            output.copyTo(bitmap);

            // clean up renderscript resources
            rs.destroy();
            input.destroy();
            output.destroy();
            script.destroy();

            return bitmap;
        }
        return null;
    }

}
