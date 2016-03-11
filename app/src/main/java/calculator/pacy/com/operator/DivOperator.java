package calculator.pacy.com.operator;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by pacy on 2016/3/11.
 */
public class DivOperator extends Operator {
    private static final int default_scale_ = 16;

    @Override
    protected double getDoubleResult(double d1, double d2) {
        return d1 / d2;
    }

    @Override
    protected BigDecimal getBigDecimalResult(BigDecimal b1, BigDecimal b2) {
        return b1.divide(b2, default_scale_, RoundingMode.HALF_UP);
    }
}
