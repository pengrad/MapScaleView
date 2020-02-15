package com.github.pengrad.mapscaleview;

import androidx.annotation.Nullable;

class Scales {
    private final Scale top, bottom;

    Scales(Scale top, Scale bottom) {
        this.top = top;
        this.bottom = bottom;
    }

    @Nullable
    Scale top() {
        return top;
    }

    @Nullable
    Scale bottom() {
        return bottom;
    }

    float maxLength() {
        return Math.max(top != null ? top.length() : 0, bottom != null ? bottom.length() : 0);
    }
}
