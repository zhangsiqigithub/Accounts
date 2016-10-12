package com.dragon.accounts.view.chart.formatter;

import com.dragon.accounts.view.chart.data.Entry;
import com.dragon.accounts.view.chart.utils.ViewPortHandler;

public interface IValueFormatter {

    /**
     * Called when a value (from labels inside the chart) is formatted
     * before being drawn. For performance reasons, avoid excessive calculations
     * and memory allocations inside this method.
     *
     * @param value           the value to be formatted
     * @param entry           the entry the value belongs to - in e.g. BarChart, this is of class BarEntry
     * @param dataSetIndex    the index of the DataSet the entry in focus belongs to
     * @param viewPortHandler provides information about the current chart state (scale, translation, ...)
     * @return the formatted label ready for being drawn
     */
    String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler);
}
