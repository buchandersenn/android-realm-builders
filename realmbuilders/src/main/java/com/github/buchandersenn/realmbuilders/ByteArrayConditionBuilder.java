package com.github.buchandersenn.realmbuilders;

public class ByteArrayConditionBuilder<B extends AbstractQueryBuilder> extends AbstractConditionBuilder<B> {

    public ByteArrayConditionBuilder(String fieldName, B builder) {
        super(fieldName, builder);
    }

    public B equalTo(byte[] value) {
        this.builder.query.equalTo(fieldName, value);
        return builder;
    }

    public B notEqualTo(byte[] value) {
        this.builder.query.notEqualTo(fieldName, value);
        return builder;
    }

    public B isEmpty(String fieldName) {
        this.builder.query.isEmpty(fieldName);
        return builder;
    }

    public B isNotEmpty(String fieldName) {
        this.builder.query.isNotEmpty(fieldName);
        return builder;
    }

    public static class Nullable<B extends AbstractQueryBuilder> extends ByteArrayConditionBuilder<B> {

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
