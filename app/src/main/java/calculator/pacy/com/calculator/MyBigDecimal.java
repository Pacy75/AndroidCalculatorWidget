package calculator.pacy.com.calculator;

import java.math.BigDecimal;

/**
 * Created by pacy on 2016/3/11.
 */
public class MyBigDecimal {
    private enum MyBigDecimalType {
        NORMAL, INFINITY, NaN;
    }

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

    private MyBigDecimal(double d) {
        bigDecimal_ = new BigDecimal(d);
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
        if (this.isInfinity() || summand.isInfinity()) {
            return MyBigDecimal.INFINITY;
        }
        if (this.isNaN() || summand.isNaN()) {
            return MyBigDecimal.NaN;
        }
        double d1 = bigDecimal_.doubleValue();
        double d2 = summand.getBigDecimal().doubleValue();
        double d3 = d1 + d2;
        if (Double.isInfinite(d3)) {
            return MyBigDecimal.INFINITY;
        }
        if (Double.isNaN(d3)) {
            return MyBigDecimal.NaN;
        }
        BigDecimal sum = this.bigDecimal_.add(summand.getBigDecimal());
        return new MyBigDecimal(sum);
    }

    public MyBigDecimal subtract(MyBigDecimal minuend) {
        if (this.isInfinity() || minuend.isInfinity()) {
            return MyBigDecimal.INFINITY;
        }
        if (this.isNaN() || minuend.isNaN()) {
            return MyBigDecimal.NaN;
        }
        double d1 = bigDecimal_.doubleValue();
        double d2 = minuend.getBigDecimal().doubleValue();
        double d3 = d1 - d2;
        if (Double.isInfinite(d3)) {
            return MyBigDecimal.INFINITY;
        }
        if (Double.isNaN(d3)) {
            return MyBigDecimal.NaN;
        }
        BigDecimal difference = this.bigDecimal_.subtract(minuend.getBigDecimal());
        return new MyBigDecimal(difference);
    }

    public MyBigDecimal multiply(MyBigDecimal multiplicand) {
        if (this.isInfinity() || multiplicand.isInfinity()) {
            return MyBigDecimal.INFINITY;
        }
        if (this.isNaN() || multiplicand.isNaN()) {
            return MyBigDecimal.NaN;
        }
        double d1 = bigDecimal_.doubleValue();
        double d2 = multiplicand.getBigDecimal().doubleValue();
        double d3 = d1 * d2;
        if (Double.isInfinite(d3)) {
            return MyBigDecimal.INFINITY;
        }
        if (Double.isNaN(d3)) {
            return MyBigDecimal.NaN;
        }
        BigDecimal product = this.bigDecimal_.multiply(multiplicand.getBigDecimal());
        return new MyBigDecimal(product);
    }

    public MyBigDecimal divide(MyBigDecimal dividend) {
        if (this.isInfinity() || dividend.isInfinity()) {
            return MyBigDecimal.INFINITY;
        }
        if (this.isNaN() || dividend.isNaN()) {
            return MyBigDecimal.NaN;
        }
        double d1 = bigDecimal_.doubleValue();
        double d2 = dividend.getBigDecimal().doubleValue();
        double d3 = d1 / d2;
        if (Double.isInfinite(d3)) {
            return MyBigDecimal.INFINITY;
        }
        if (Double.isNaN(d3)) {
            return MyBigDecimal.NaN;
        }
        return new MyBigDecimal(d3);
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
