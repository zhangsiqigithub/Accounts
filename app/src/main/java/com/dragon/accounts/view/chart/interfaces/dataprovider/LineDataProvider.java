package com.dragon.accounts.view.chart.interfaces.dataprovider;


import com.dragon.accounts.view.chart.components.YAxis;
import com.dragon.accounts.view.chart.data.LineData;

public interface LineDataProvider extends BarLineScatterCandleBubbleDataProvider {

    LineData getLineData();

    YAxis getAxis(YAxis.AxisDependency dependency);
}
