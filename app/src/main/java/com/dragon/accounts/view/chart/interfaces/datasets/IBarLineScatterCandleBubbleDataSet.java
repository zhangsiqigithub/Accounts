package com.dragon.accounts.view.chart.interfaces.datasets;

import com.dragon.accounts.view.chart.data.Entry;

public interface IBarLineScatterCandleBubbleDataSet<T extends Entry> extends IDataSet<T> {

    /**
     * Returns the color that is used for drawing the highlight indicators.
     *
     * @return
     */
    int getHighLightColor();
}
