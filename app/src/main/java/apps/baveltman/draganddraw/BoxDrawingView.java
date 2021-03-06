package apps.baveltman.draganddraw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by borisvelt on 3/28/15.
 */
public class BoxDrawingView extends View {

    public static final String TAG = "BoxDrawingView";
    public static final String KEY_BOXES = "apps.baveltman.DragAndDraw.KEY_BOXES";
    public static final String KEY_STATE = "apps.baveltman.DragAndDraw.KEY_STATE";

    private Box mCurrentBox;
    private ArrayList<Box> mBoxes = new ArrayList<Box>();
    private Paint mBoxPaint;
    private Paint mBackgroundPaint;

    // Used when creating the view in code
    public BoxDrawingView(Context context) {
        this(context, null);
    }

    // Used when inflating the view from XML
    public BoxDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Paint the boxes a nice semitransparent red (ARGB)
        mBoxPaint = new Paint();
        mBoxPaint.setColor(0x22ff0000);

        // Paint the background off-white
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(0xfff8efe0);

    }

    @Override
    public Parcelable onSaveInstanceState() {

        Bundle savedInstanceState = new Bundle();
        savedInstanceState.putParcelableArrayList(KEY_BOXES, mBoxes);
        savedInstanceState.putParcelable(KEY_STATE, super.onSaveInstanceState());
        return savedInstanceState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable savedInstanceState) {


        if (savedInstanceState == null) return;

        if (savedInstanceState instanceof Bundle) {
            Bundle savedState = (Bundle) savedInstanceState;
            mBoxes = savedState.getParcelableArrayList(KEY_BOXES);

            Parcelable state = savedState.getParcelable(KEY_STATE);
            super.onRestoreInstanceState(state);
        }
    }

    public boolean onTouchEvent(MotionEvent event) {

        if (event.getPointerCount() == 2){
            //clear screen
            mBoxes = new ArrayList<Box>();
            invalidate();
        } else {
            //draw box
            PointF curr = new PointF(event.getX(), event.getY());

            Log.i(TAG, "Received event at x=" + curr.x +
                    ", y=" + curr.y + ":");

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Log.i(TAG, " ACTION_DOWN");
                    // Reset drawing state and create a new box for view
                    mCurrentBox = new Box(curr);
                    mBoxes.add(mCurrentBox);
                    break;

                case MotionEvent.ACTION_MOVE:
                    Log.i(TAG, " ACTION_MOVE");
                    if (mCurrentBox != null) {
                        mCurrentBox.setCurrent(curr);
                        //call invalidate to force redrawing of view with new box
                        invalidate();
                    }
                    break;

                case MotionEvent.ACTION_UP:
                    Log.i(TAG, " ACTION_UP");
                    mCurrentBox = null;
                    break;

                case MotionEvent.ACTION_CANCEL:
                    Log.i(TAG, " ACTION_CANCEL");
                    mCurrentBox = null;
                    break;
            }
        }

        return true;
    }

    /**
     * goes through all boxes and draws them on screen
     */
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPaint(mBackgroundPaint);
        for (Box box : mBoxes) {
            float left = Math.min(box.getOrigin().x, box.getCurrent().x);
            float right = Math.max(box.getOrigin().x, box.getCurrent().x);
            float top = Math.min(box.getOrigin().y, box.getCurrent().y);
            float bottom = Math.max(box.getOrigin().y, box.getCurrent().y);
            canvas.drawRect(left, top, right, bottom, mBoxPaint);
        }
    }
}
