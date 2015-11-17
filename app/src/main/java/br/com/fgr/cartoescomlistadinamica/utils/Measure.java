package br.com.fgr.cartoescomlistadinamica.utils;

import android.content.Context;
import android.util.TypedValue;

public class Measure {

    public static int getPixelsFromDP(Context context, int dp) {

        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());

    }

    public static int getPixelsFromSP(Context context, int sp) {

        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                context.getResources().getDisplayMetrics());

    }

}