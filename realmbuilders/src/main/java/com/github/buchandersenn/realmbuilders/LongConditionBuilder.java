package com.github.buchandersenn.realmbuilders;

public class LongConditionBuilder<B extends AbstractQueryBuilder> extends AbstractConditionBuilder<B> {

    public LongConditionBuilder(String fieldName, B builder) {
        super(fieldName, builder);
    }

    public B equalTo(long value) {
        this.builder.query.equalTo(fieldName, value);
        return builder;
    }

    public B notEqualTo(long value) {
        this.builder.query.notEqualTo(fieldName, value);
        return builder;
    }

    public B between(long value1, long value2) {
        this.builder.query.between(fieldName, value1, value2);
        return builder;
    }

    public B greaterThan(long value) {
        this.builder.query.greaterThan(fieldName, value);
        return builder;
    }

    public B greaterThanOrEqualTo(long value) {
        this.builder.query.greaterThanOrEqualTo(fieldName, value);
        return builder;
    }

    public B lessThan(long value) {
        this.builder.query.lessThan(fieldName, value);
        return builder;
    }

    public B lessThanOrEqualTo(long value) {
        this.builder.query.lessThanOrEqualTo(fieldName, value);
        return builder;
    }

    public B in(Long[] values) {
        this.builder.query.in(fieldName, values);
        return builder;
    }

    public Number min() {
        return builder.query.min(fieldName);
    }

    public Number max() {
        return builder.query.max(fieldName);
    }

    public double average() {
        return builder.query.average(fieldName);
    }

    public Number sum() {
        return builder.query.sum(fieldName);
    }

    public static class Nullable<B extends AbstractQueryBuilder> extends LongConditionBuilder<B> {

        public Nullable(String fieldName, B builder) {
            super(fieldName, builder);
        }

        public B equalTo(Long value) {
            this.builder.query.equalTo(fieldName, value);
            return builder;
        }

        public B notEqualTo(Long value) {
            this.builder.query.notEqualTo(fieldName, value);
            return builder;
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
