package apps.baveltman.draganddraw;

import android.graphics.PointF;

/**
 * Model that defines a rectangular box
 */
public class Box {
    private PointF mOrigin;
    private PointF mCurrent;

    public Box(PointF origin) {
        mOrigin = mCurrent = origin;
    }

    public void setCurrent(PointF current) {
        mCurrent = current;
    }

    public PointF getCurrent(){
        return mCurrent;
    }

    public PointF getOrigin() {
        return mOrigin;
    }
}
