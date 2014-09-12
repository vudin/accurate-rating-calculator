package com.androtips.ratingcalculator.anim;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;

public class Rotate3dAnimation extends Animation {
    private static final int DEFAULT_DURATION = 150;

    private final float _fromDegrees;
    private final float _toDegrees;
    private final float _centerX;
    private final float _centerY;
    private final float _depthZ;
    private final boolean _reverse;
    private Camera mCamera;

    public Rotate3dAnimation(float p_fromDegrees, float p_toDegrees,
            float p_centerX, float p_centerY, float p_depthZ, boolean p_reverse) {
        _fromDegrees = p_fromDegrees;
        _toDegrees = p_toDegrees;
        _centerX = p_centerX;
        _centerY = p_centerY;
        _depthZ = p_depthZ;
        _reverse = p_reverse;
    }

    @Override
    public void initialize(int width, int height, int parentWidth,
            int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        mCamera = new Camera();
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        final float fromDegrees = _fromDegrees;
        float degrees = fromDegrees
                + ((_toDegrees - fromDegrees) * interpolatedTime);

        final float centerX = _centerX;
        final float centerY = _centerY;
        final Camera camera = mCamera;

        final Matrix matrix = t.getMatrix();

        camera.save();
        if (_reverse) {
            camera.translate(0.0f, 0.0f, _depthZ * interpolatedTime);
        } else {
            camera.translate(0.0f, 0.0f, _depthZ * (1.0f - interpolatedTime));
        }
        camera.rotateY(degrees);
        camera.getMatrix(matrix);
        camera.restore();

        matrix.preTranslate(-centerX, -centerY);
        matrix.postTranslate(centerX, centerY);
    }

    public static void applyRotation(final View p_view1, final View p_view2,
            float p_start, float p_end) {
        final View container = (View) p_view1.getParent();
        final float centerX = container.getWidth() / 2.0f;
        final float centerY = container.getHeight() / 2.0f;

        final Rotate3dAnimation rotation = new Rotate3dAnimation(p_start,
                p_end, centerX, centerY, 310.0f, true);
        rotation.setDuration(DEFAULT_DURATION);
        rotation.setFillAfter(true);
        rotation.setInterpolator(new AccelerateInterpolator());
        rotation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                container.post(new SwapViews(container, p_view1, p_view2));
            }
        });
        container.startAnimation(rotation);
    }

    static class SwapViews implements Runnable {
        private View container;
        private View view1;
        private View view2;

        public SwapViews(View p_container, View p_view1, View p_view2) {
            container = p_container;
            view1 = p_view1;
            view2 = p_view2;
        }

        public void run() {
            final float centerX = container.getWidth() / 2.0f;
            final float centerY = container.getHeight() / 2.0f;
            Rotate3dAnimation anim;

            view1.setVisibility(View.GONE);
            view2.setVisibility(View.VISIBLE);
            view2.requestFocus();

            anim = new Rotate3dAnimation(90, -90, centerX, centerY, 310.0f,
                    false);
            anim.setDuration(0);
            anim.setFillAfter(true);
            container.startAnimation(anim);

            anim = new Rotate3dAnimation(-90, 0, centerX, centerY, 310.0f,
                    false);

            anim.setDuration(DEFAULT_DURATION);
            anim.setFillAfter(true);
            anim.setInterpolator(new DecelerateInterpolator());

            container.startAnimation(anim);
        }
    }
}
