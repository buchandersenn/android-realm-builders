package dk.nicolai_buch_andersen.realmbuilders;

public class FloatConditionBuilder<B extends AbstractQueryBuilder> extends AbstractConditionBuilder<B> {

    public FloatConditionBuilder(String fieldName, B builder) {
        super(fieldName, builder);
    }

    public B equalTo(float value) {
        this.builder.query.equalTo(fieldName, value);
        return builder;
    }

    public B notEqualTo(float value) {
        this.builder.query.notEqualTo(fieldName, value);
        return builder;
    }

    public B between(float value1, float value2) {
        this.builder.query.between(fieldName, value1, value2);
        return builder;
    }

    public B greaterThan(float value) {
        this.builder.query.greaterThan(fieldName, value);
        return builder;
    }

    public B greaterThanOrEqualTo(float value) {
        this.builder.query.greaterThanOrEqualTo(fieldName, value);
        return builder;
    }

    public B lessThan(float value) {
        this.builder.query.lessThan(fieldName, value);
        return builder;
    }

    public B lessThanOrEqualTo(float value) {
        this.builder.query.lessThanOrEqualTo(fieldName, value);
        return builder;
    }

    public B in(Float[] values) {
        this.builder.query.in(fieldName, values);
        return builder;
    }

    public static class Nullable<B extends AbstractQueryBuilder> extends FloatConditionBuilder<B> {

        public Nullable(String fieldName, B builder) {
            super(fieldName, builder);
        }

        public B equalTo(Float value) {
            this.builder.query.equalTo(fieldName, value);
            return builder;
        }

        public B notEqualTo(Float value) {
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
