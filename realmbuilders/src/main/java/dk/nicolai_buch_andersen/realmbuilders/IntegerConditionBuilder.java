package dk.nicolai_buch_andersen.realmbuilders;

public class IntegerConditionBuilder<B extends AbstractQueryBuilder> extends AbstractConditionBuilder<B> {

    public IntegerConditionBuilder(String fieldName, B builder) {
        super(fieldName, builder);
    }

    public B equalTo(int value) {
        this.builder.query.equalTo(fieldName, value);
        return builder;
    }

    public B notEqualTo(int value) {
        this.builder.query.notEqualTo(fieldName, value);
        return builder;
    }

    public B between(int value1, int value2) {
        this.builder.query.between(fieldName, value1, value2);
        return builder;
    }

    public B greaterThan(int value) {
        this.builder.query.greaterThan(fieldName, value);
        return builder;
    }

    public B greaterThanOrEqualTo(int value) {
        this.builder.query.greaterThanOrEqualTo(fieldName, value);
        return builder;
    }

    public B lessThan(int value) {
        this.builder.query.lessThan(fieldName, value);
        return builder;
    }

    public B lessThanOrEqualTo(int value) {
        this.builder.query.lessThanOrEqualTo(fieldName, value);
        return builder;
    }

    public B in(Integer[] values) {
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

    public static class Nullable<B extends AbstractQueryBuilder> extends IntegerConditionBuilder<B> {

        public Nullable(String fieldName, B builder) {
            super(fieldName, builder);
        }

        public B equalTo(Integer value) {
            this.builder.query.equalTo(fieldName, value);
            return builder;
        }

        public B notEqualTo(Integer value) {
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
