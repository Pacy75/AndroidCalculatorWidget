package calculator.pacy.com.operator;

import java.math.BigDecimal;

import calculator.pacy.com.calculator.MyBigDecimal;

/**
 * Created by pacy on 2016/3/11.
 */
public abstract class Operator {
    protected abstract double getDoubleResult(double d1, double d2);

    protected abstract BigDecimal getBigDecimalResult(BigDecimal b1, BigDecimal b2);


    public MyBigDecimal get(MyBigDecimal mb1, MyBigDecimal mb2) {
        if (mb1.isInfinity() || mb2.isInfinity()) {
            return MyBigDecimal.INFINITY;
        }
        if (mb1.isNaN() || mb2.isNaN()) {
            return MyBigDecimal.NaN;
        }
        double d1 = mb1.getBigDecimal().doubleValue();
        double d2 = mb2.getBigDecimal().doubleValue();
        double d3 = getDoubleResult(d1, d2);
        if (Double.isInfinite(d3)) {
            return MyBigDecimal.INFINITY;
        }
        if (Double.isNaN(d3)) {
            return MyBigDecimal.NaN;
        }
        BigDecimal result = getBigDecimalResult(mb1.getBigDecimal(), mb2.getBigDecimal());
        return new MyBigDecimal(result);
    }
}
