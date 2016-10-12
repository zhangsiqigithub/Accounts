package com.dragon.accounts.view.chart.formatter;

import com.dragon.accounts.view.chart.interfaces.dataprovider.LineDataProvider;
import com.dragon.accounts.view.chart.interfaces.datasets.ILineDataSet;

public interface IFillFormatter {

    /**
     * Returns the vertical (y-axis) position where the filled-line of the
     * LineDataSet should end.
     *
     * @param dataSet      the ILineDataSet that is currently drawn
     * @param dataProvider
     * @return
     */
    float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider);
}
