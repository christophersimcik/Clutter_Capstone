package com.clutter.note.main;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by csimcik on 2/23/2018.
 */
public class ShadowCircle extends View  {
Paint paint;
    RectF rect;
    int colorVal = 100;
    public ShadowCircle(Context context, AttributeSet set) {
      super(context, set);
        paint = new Paint();
        rect = new RectF(0,0,0,0);
        paint.setAntiAlias(true);
        paint.setColor(Color.argb(colorVal,0,0,0));
    }

    @Override
    public void onDraw(Canvas canvas){
        paint.setColor(Color.argb(colorVal,0,0,0));
            canvas.drawRoundRect(rect, 20, 20, paint);

}
    public void setParams(int viewsWidth){
     rect = new RectF(0,0,viewsWidth,viewsWidth);
        invalidate();
    }
    public void setVals(int colorAdj){
        colorVal = colorAdj;
        invalidate();
    }
}
