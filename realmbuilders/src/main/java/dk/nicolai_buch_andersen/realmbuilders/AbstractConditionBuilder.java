package dk.nicolai_buch_andersen.realmbuilders;

public class AbstractConditionBuilder<B extends AbstractQueryBuilder> {
    protected String fieldName;
    protected B builder;

    public AbstractConditionBuilder(String fieldName, B builder) {
        this.fieldName = fieldName;
        this.builder = builder;
    }
}
