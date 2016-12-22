package dk.nicolai_buch_andersen.realmbuilders;

import java.util.Date;

public class DateConditionBuilder<B extends AbstractQueryBuilder> extends AbstractConditionBuilder<B> {

    public DateConditionBuilder(String fieldName, B builder) {
        super(fieldName, builder);
    }

    public B equalTo(Date value) {
        this.builder.query.equalTo(fieldName, value);
        return builder;
    }

    public B notEqualTo(Date value) {
        this.builder.query.notEqualTo(fieldName, value);
        return builder;
    }

    public B between(Date value1, Date value2) {
        this.builder.query.between(fieldName, value1, value2);
        return builder;
    }

    public B greaterThan(Date value) {
        this.builder.query.greaterThan(fieldName, value);
        return builder;
    }

    public B greaterThanOrEqualTo(Date value) {
        this.builder.query.greaterThanOrEqualTo(fieldName, value);
        return builder;
    }

    public B lessThan(Date value) {
        this.builder.query.lessThan(fieldName, value);
        return builder;
    }

    public B lessThanOrEqualTo(Date value) {
        this.builder.query.lessThanOrEqualTo(fieldName, value);
        return builder;
    }

    public B in(Date[] values) {
        this.builder.query.in(fieldName, values);
        return builder;
    }

    public Date min() {
        return builder.query.minimumDate(fieldName);
    }

    public Date max() {
        return builder.query.maximumDate(fieldName);
    }

    public static class Nullable<B extends AbstractQueryBuilder> extends DateConditionBuilder<B> {

        public Nullable(String fieldName, B builder) {
            super(fieldName, builder);
        }

        public B isNull() {
            this.builder.query.isNull(fieldName);
            return builder;
        }

        public B isNotNull() {
            this.builder.query.isNotNull(fieldName);
            return builder;
        }
    }
}
