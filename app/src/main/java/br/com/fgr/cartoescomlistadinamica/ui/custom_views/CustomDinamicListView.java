package br.com.fgr.cartoescomlistadinamica.ui.custom_views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;

public class CustomDinamicListView extends DynamicListView {

    private final Camera mCamera = new Camera();
    private final Matrix mMatrix = new Matrix();
    /**
     * Paint object to draw with
     */
    private Paint mPaint;

    public CustomDinamicListView(Context context) {
        super(context);
    }

    public CustomDinamicListView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public CustomDinamicListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {

        Bitmap bitmap;

        // center point of child
        final int childCenterY = child.getHeight() / 2;

        //center of list
        final int parentCenterY = getHeight() - 40
         /*/ 2*/;

        //center point of child relative to list
        final int absChildCenterY = child.getTop() + childCenterY;

        //distance of child center to the list center
        final int distanceY = parentCenterY - absChildCenterY;

        //radius of imaginary cirlce
        final int r = getHeight() / 2;

        prepareMatrix(mMatrix, distanceY, r, child.getHeight());

        bitmap = getChildDrawingCache(childCenterY == absChildCenterY, child);
        canvas.drawBitmap(bitmap, mMatrix, mPaint);

        return false;

    }

    private void prepareMatrix(final Matrix outMatrix, float distanceY, float r, float heightChild) {

        mCamera.save();

        float visibility = ((distanceY / r) * (2 * heightChild / 3));

        mCamera.getMatrix(outMatrix);
        outMatrix.setTranslate(0, (heightChild - visibility));
        mCamera.restore();

    }

    private Bitmap getChildDrawingCache(boolean drawShadow, final View child) {

        Bitmap bitmap = child.getDrawingCache();

        if (bitmap == null) {

            child.setDrawingCacheEnabled(true);
            child.buildDrawingCache();
            bitmap = child.getDrawingCache();

        }

//        if (!drawShadow)
//            bitmap = addShadow(bitmap, bitmap.getHeight(), bitmap.getWidth(), Color.BLACK, 3, 5, 5);

        return bitmap;

    }

    private Bitmap addShadow(final Bitmap bm, final int dstHeight, final int dstWidth, int color,
                             int size, float dx, float dy) {

        final Bitmap mask = Bitmap.createBitmap(dstWidth, dstHeight, Bitmap.Config.ALPHA_8);

        final Matrix scaleToFit = new Matrix();
        final RectF src = new RectF(0, 0, bm.getWidth(), bm.getHeight());
        final RectF dst = new RectF(0, 0, dstWidth - dx, dstHeight - dy);
        scaleToFit.setRectToRect(src, dst, Matrix.ScaleToFit.CENTER);

        final Matrix dropShadow = new Matrix(scaleToFit);
        dropShadow.postTranslate(dx, dy);

        final Canvas maskCanvas = new Canvas(mask);
        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        maskCanvas.drawBitmap(bm, scaleToFit, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        maskCanvas.drawBitmap(bm, dropShadow, paint);

        final BlurMaskFilter filter = new BlurMaskFilter(size, BlurMaskFilter.Blur.NORMAL);
        paint.reset();
        paint.setAntiAlias(true);
        paint.setColor(color);
        paint.setMaskFilter(filter);
        paint.setFilterBitmap(true);

        final Bitmap ret = Bitmap.createBitmap(dstWidth, dstHeight, Bitmap.Config.ARGB_8888);
        final Canvas retCanvas = new Canvas(ret);
        retCanvas.drawBitmap(mask, 0, 0, paint);
        retCanvas.drawBitmap(bm, scaleToFit, null);
        mask.recycle();

        return ret;

    }

}