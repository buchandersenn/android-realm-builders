package com.github.buchandersenn.realmbuilders;

public class ByteConditionBuilder<B extends AbstractQueryBuilder> extends AbstractConditionBuilder<B> {

    public ByteConditionBuilder(String fieldName, B builder) {
        super(fieldName, builder);
    }

    public B equalTo(byte value) {
        this.builder.query.equalTo(fieldName, value);
        return builder;
    }

    public B notEqualTo(byte value) {
        this.builder.query.notEqualTo(fieldName, value);
        return builder;
    }

    public B in(Byte[] values) {
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

    public static class Nullable<B extends AbstractQueryBuilder> extends ByteConditionBuilder<B> {

        public Nullable(String fieldName, B builder) {
            super(fieldName, builder);
        }

        public B equalTo(Byte value) {
            this.builder.query.equalTo(fieldName, value);
            return builder;
        }

        public B notEqualTo(Byte value) {
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
