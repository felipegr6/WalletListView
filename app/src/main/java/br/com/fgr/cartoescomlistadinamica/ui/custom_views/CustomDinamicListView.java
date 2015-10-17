package br.com.fgr.cartoescomlistadinamica.ui.custom_views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;

public class CustomDinamicListView extends DynamicListView {

    /**
     * Ambient light intensity
     */
    private static final int AMBIENT_LIGHT = 55;
    /**
     * Diffuse light intensity
     */
    private static final int DIFFUSE_LIGHT = 200;
    /**
     * Specular light intensity
     */
    private static final float SPECULAR_LIGHT = 70;
    /**
     * Shininess constant
     */
    private static final float SHININESS = 200;
    /**
     * The max intensity of the light
     */
    private static final int MAX_INTENSITY = 0xFF;

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

        Bitmap bitmap = getChildDrawingCache(child);

        // center point of child
        final int childCenterY = child.getHeight() / 2;

        //center of list
        final int parentCenterY = getHeight() / 2;

        //center point of child relative to list
        final int absChildCenterY = child.getTop() + childCenterY;

        //distance of child center to the list center
        final int distanceY = parentCenterY - absChildCenterY;

        //radius of imaginary cirlce
        final int r = getHeight() / 2;

        prepareMatrix(mMatrix, distanceY, r, child.getHeight());

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

    private Bitmap getChildDrawingCache(final View child) {
        Bitmap bitmap = child.getDrawingCache();
        if (bitmap == null) {
            child.setDrawingCacheEnabled(true);
            child.buildDrawingCache();
            bitmap = child.getDrawingCache();
        }
        return bitmap;
    }

}