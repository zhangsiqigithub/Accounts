
package com.dragon.accounts.view.chart.renderer;

import com.dragon.accounts.view.chart.utils.ViewPortHandler;

public abstract class Renderer {

    /**
     * the component that handles the drawing area of the chart and it's offsets
     */
    protected ViewPortHandler mViewPortHandler;

    public Renderer(ViewPortHandler viewPortHandler) {
        this.mViewPortHandler = viewPortHandler;
    }
}
