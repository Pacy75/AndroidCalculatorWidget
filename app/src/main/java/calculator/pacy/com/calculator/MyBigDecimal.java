package calculator.pacy.com.calculator;

import java.math.BigDecimal;

import calculator.pacy.com.operator.AddOperator;
import calculator.pacy.com.operator.DivOperator;
import calculator.pacy.com.operator.MulOperator;
import calculator.pacy.com.operator.Operator;
import calculator.pacy.com.operator.SubOperator;

/**
 * Created by pacy on 2016/3/11.
 */
public class MyBigDecimal {
    private enum MyBigDecimalType {
        NORMAL, INFINITY, NaN
    }

    private static final Operator addOperator_ = new AddOperator();
    private static final Operator subOperator_ = new SubOperator();
    private static final Operator mulOperator_ = new MulOperator();
    private static final Operator divOperator_ = new DivOperator();
    private static final String STR_INFINITY = "Infinity";
    private static final String STR_NaN = "NaN";
    public static final MyBigDecimal ZERO = new MyBigDecimal(BigDecimal.ZERO);
    public static final MyBigDecimal INFINITY = new MyBigDecimal(MyBigDecimalType.INFINITY);
    public static final MyBigDecimal NaN = new MyBigDecimal(MyBigDecimalType.NaN);

    private BigDecimal bigDecimal_;
    private MyBigDecimalType type_ = MyBigDecimalType.NORMAL;

    public MyBigDecimal(String s) {
        if (STR_INFINITY.equals(s)) {
            type_ = MyBigDecimalType.INFINITY;
            return;
        }
        if (STR_NaN.equals(s)) {
            type_ = MyBigDecimalType.NaN;
            return;
        }
        try {
            bigDecimal_ = new BigDecimal(s);
        } catch (NumberFormatException e) {
            type_ = MyBigDecimalType.NaN;
        }
    }

    public MyBigDecimal(BigDecimal bigDecimal) {
        bigDecimal_ = bigDecimal;
    }

    private MyBigDecimal(MyBigDecimalType type) {
        bigDecimal_ = BigDecimal.ZERO;
        type_ = type;
    }

    public BigDecimal getBigDecimal() {
        return bigDecimal_;
    }

    public MyBigDecimal add(MyBigDecimal summand) {
        return addOperator_.get(this, summand);
    }

    public MyBigDecimal subtract(MyBigDecimal minuend) {
        return subOperator_.get(this, minuend);
    }

    public MyBigDecimal multiply(MyBigDecimal multiplicand) {
        return mulOperator_.get(this, multiplicand);
    }

    public MyBigDecimal divide(MyBigDecimal dividend) {
        return divOperator_.get(this, dividend);
    }

    @Override
    public String toString() {
        if (this.isInfinity()) {
            return STR_INFINITY;
        }
        if (this.isNaN()) {
            return STR_NaN;
        }
        double d = bigDecimal_.doubleValue();
        if (d == (long) d)
            return String.format("%d", (long) d);
        else
            return String.format("%s", d);
    }

    public boolean isInfinity() {
        return MyBigDecimalType.INFINITY.equals(type_);
    }

    public boolean isNaN() {
        return MyBigDecimalType.NaN.equals(type_);
    }
}
