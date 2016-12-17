package dk.nicolai_buch_andersen.realmbuilders;

/**
 * Condition builder for (non-primitive, nullable) Boolean fields.
 */
public class BooleanConditionBuilder<B extends AbstractQueryBuilder> extends AbstractConditionBuilder<B> {

    public BooleanConditionBuilder(String fieldName, B builder) {
        super(fieldName, builder);
    }

    public B equalTo(boolean value) {
        this.builder.query.equalTo(fieldName, value);
        return builder;
    }

    public B notEqualTo(boolean value) {
        this.builder.query.notEqualTo(fieldName, value);
        return builder;
    }

    public B in(Boolean[] values) {
        this.builder.query.in(fieldName, values);
        return builder;
    }

    public static class Nullable<B extends AbstractQueryBuilder> extends BooleanConditionBuilder<B> {

        public Nullable(String fieldName, B builder) {
            super(fieldName, builder);
        }

        public B equalTo(Boolean value) {
            this.builder.query.equalTo(fieldName, value);
            return builder;
        }

        public B notEqualTo(Boolean value) {
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
