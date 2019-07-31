package uk.me.mattgill.samples.richfaces;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.richfaces.model.ChartDataModel.ChartType;
import org.richfaces.model.StringChartDataModel;

@Named
@RequestScoped
public class PieChart {

    private final StringChartDataModel pie = new StringChartDataModel(ChartType.pie);

    @Inject
    private RandomNumberGenerator generator;

    @PostConstruct
    private void init() {
        pie.put("Person 1", generator.getNumberBetween(0, 100));
        pie.put("Person 2", generator.getNumberBetween(0, 100));
        pie.put("Person 3", generator.getNumberBetween(0, 100));
    }

    public StringChartDataModel getPie() {
        return pie;
    }

}