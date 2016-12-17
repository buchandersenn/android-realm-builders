package dk.nicolai_buch_andersen.realmbuilders;

public class ShortConditionBuilder<B extends AbstractQueryBuilder> extends AbstractConditionBuilder<B> {

    public ShortConditionBuilder(String fieldName, B builder) {
        super(fieldName, builder);
    }

    public B equalTo(short value) {
        this.builder.query.equalTo(fieldName, value);
        return builder;
    }

    public B notEqualTo(short value) {
        this.builder.query.notEqualTo(fieldName, value);
        return builder;
    }

    public B between(short value1, short value2) {
        this.builder.query.between(fieldName, value1, value2);
        return builder;
    }

    public B greaterThan(short value) {
        this.builder.query.greaterThan(fieldName, value);
        return builder;
    }

    public B greaterThanOrEqualTo(short value) {
        this.builder.query.greaterThanOrEqualTo(fieldName, value);
        return builder;
    }

    public B lessThan(short value) {
        this.builder.query.lessThan(fieldName, value);
        return builder;
    }

    public B lessThanOrEqualTo(short value) {
        this.builder.query.lessThanOrEqualTo(fieldName, value);
        return builder;
    }

    public B in(Short[] values) {
        this.builder.query.in(fieldName, values);
        return builder;
    }

    public static class Nullable<B extends AbstractQueryBuilder> extends ShortConditionBuilder<B> {

        public Nullable(String fieldName, B builder) {
            super(fieldName, builder);
        }

        public B equalTo(Short value) {
            this.builder.query.equalTo(fieldName, value);
            return builder;
        }

        public B notEqualTo(Short value) {
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
