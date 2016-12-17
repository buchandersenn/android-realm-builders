package dk.nicolai_buch_andersen.realmbuilders;

import io.realm.Case;

public class StringConditionBuilder<B extends AbstractQueryBuilder> extends AbstractConditionBuilder<B> {

    public StringConditionBuilder(String fieldName, B builder) {
        super(fieldName, builder);
    }

    public B equalTo(String value) {
        this.builder.query.equalTo(fieldName, value);
        return builder;
    }

    public B notEqualTo(String value) {
        this.builder.query.notEqualTo(fieldName, value);
        return builder;
    }

    public B beginsWith(String value) {
        this.builder.query.beginsWith(fieldName, value);
        return builder;
    }

    public B beginsWith(String value, Case casing) {
        this.builder.query.beginsWith(fieldName, value, casing);
        return builder;
    }

    public B endsWith(String value) {
        this.builder.query.endsWith(fieldName, value);
        return builder;
    }

    public B endsWith(String value, Case casing) {
        this.builder.query.endsWith(fieldName, value, casing);
        return builder;
    }

    public B contains(String value) {
        this.builder.query.contains(fieldName, value);
        return builder;
    }

    public B contains(String value, Case casing) {
        this.builder.query.contains(fieldName, value, casing);
        return builder;
    }

    public B in(String[] values) {
        this.builder.query.in(fieldName, values);
        return builder;
    }

    public B in(String[] values, Case casing) {
        this.builder.query.in(fieldName, values, casing);
        return builder;
    }

    public B isEmpty() {
        this.builder.query.isEmpty(fieldName);
        return builder;
    }

    public B isNotEmpty() {
        this.builder.query.isNotEmpty(fieldName);
        return builder;
    }

    public static class Nullable<B extends AbstractQueryBuilder> extends StringConditionBuilder<B> {

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
