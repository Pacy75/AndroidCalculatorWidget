package calculator.pacy.com.operator;

import java.math.BigDecimal;

/**
 * Created by pacy on 2016/3/11.
 */
public class MulOperator extends Operator {

    @Override
    protected double getDoubleResult(double d1, double d2) {
        return d1 * d2;
    }

    @Override
    protected BigDecimal getBigDecimalResult(BigDecimal b1, BigDecimal b2) {
        return b1.multiply(b2);
    }
}
