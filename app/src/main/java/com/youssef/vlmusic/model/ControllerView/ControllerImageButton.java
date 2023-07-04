package com.youssef.vlmusic.model.ControllerView;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class ControllerImageButton extends androidx.appcompat.widget.AppCompatImageButton {


    public OnPressDownAction onPressDownAction = ()->{};
    public OnPressUpAction onPressUpAction = ()->{};
//    private Animation scaleAnimation ;


    public ControllerImageButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        setScaleAnimation();
    }


    public ControllerImageButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
//        setScaleAnimation();
    }


    public ControllerImageButton(@NonNull Context context) {
        super(context);
//        setScaleAnimation();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
//        final Context context = getContext();
        performClick();
        if (event.getAction() == MotionEvent.ACTION_DOWN){
//            setColorFilter(ContextCompat.getColor(context, R.color.white));
            onPressDownAction.onPressDown();
        }else if (event.getAction() == MotionEvent.ACTION_UP){
//            setColorFilter(ContextCompat.getColor(context, R.color.black));
            onPressUpAction.onPressUp();
        }
        return true;
    }


//    public void setOnPressDownAction(OnPressDownAction onPressDownAction) {
//        this.onPressDownAction = onPressDownAction;
//    }


    public void setOnPressUpAction(OnPressUpAction onPressUpAction) {
        this.onPressUpAction = onPressUpAction;
    }

    /*private void setScaleAnimation(){
        scaleAnimation = new ScaleAnimation(
                1f, 2f, 1f, 2f
        );
        scaleAnimation.setDuration(1000);
    }*/


    @Override
    public boolean performClick() {
        return super.performClick();
    }


}
