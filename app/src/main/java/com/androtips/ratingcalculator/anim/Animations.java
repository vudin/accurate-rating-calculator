package com.androtips.ratingcalculator.anim;

import android.view.View;

public class Animations {
    public static void rotate3d(View p_view1, View p_view2) {
        Rotate3dAnimation.applyRotation(p_view1, p_view2, 0, 90);
    }
}
