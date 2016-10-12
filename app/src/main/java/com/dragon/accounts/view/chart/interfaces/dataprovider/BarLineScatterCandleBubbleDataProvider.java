package com.dragon.accounts.view.chart.interfaces.dataprovider;

import com.dragon.accounts.view.chart.components.YAxis;
import com.dragon.accounts.view.chart.data.BarLineScatterCandleBubbleData;
import com.dragon.accounts.view.chart.utils.Transformer;

public interface BarLineScatterCandleBubbleDataProvider extends ChartInterface {

    Transformer getTransformer(YAxis.AxisDependency axis);

    boolean isInverted(YAxis.AxisDependency axis);

    float getLowestVisibleX();

    float getHighestVisibleX();

    BarLineScatterCandleBubbleData getData();
}
