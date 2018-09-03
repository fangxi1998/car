package com.example.fangxi1998.update;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;


import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class Charts {


    private LineChartView lineChart;
    private LineChartData data = new LineChartData();
    private List<Line> lines = new ArrayList<Line>();

    public Charts(LineChartView lineChart) {

        int xLength = 20;
        int yLength = 50;
        int subX = 1;
        int subY = 2;

        this.lineChart = lineChart;
        data.setLines(lines);


        Axis axisX = new Axis();
        axisX.setTextSize(10);
        axisX.setTextColor(Color.rgb(55, 93, 93));
        List<AxisValue> Xtab = new ArrayList<AxisValue>();
        for (int i = 0; i < xLength; i += subX) {
            Xtab.add(new AxisValue(i).setLabel("" + i));
        }
        axisX.setValues(Xtab);
        axisX.setHasLines(true);
        axisX.setName("");


        Axis axisY = new Axis();
        axisY.setTextColor(Color.rgb(55, 93, 93));
        List<AxisValue> Ytab = new ArrayList<AxisValue>();
        for (int i = 0; i < yLength; i += subY) {
            Ytab.add(new AxisValue(i).setLabel("" + i));
        }
        axisY.setValues(Ytab);
        axisY.setTextSize(10);
        axisY.setHasLines(false);
        axisY.setName("实时体温ֵ");
        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);



        Viewport v = new Viewport(lineChart.getMaximumViewport());
        v.bottom = 0;
        v.top = yLength;
        v.left = 0;
        v.right = xLength;
        lineChart.setMaximumViewport(v);
        lineChart.setCurrentViewport(v);
        lineChart.setZoomEnabled(false);
    }

    public void flush() {
        lineChart.setLineChartData(data);


    }

    public class LINE {
        List<PointValue> Xval = new ArrayList<PointValue>();
        Line line = new Line(Xval);

        public LINE(String color) {
            line.setColor(Color.parseColor(color));
            line.setStrokeWidth(2);
            line.setCubic(true);

            line.setHasLines(true);
            line.setHasPoints(true);
            line.setHasLabels(true);
            lines.add(line);
        }

        public void add(float x, float y) {
            Xval.add(new PointValue(x, y));
        }

        public void clean() {
            Xval.clear();
        }
    }
}