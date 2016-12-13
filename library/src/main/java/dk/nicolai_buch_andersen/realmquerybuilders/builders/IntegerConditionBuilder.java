//package dk.nicolai_buch_andersen.realmquerybuilders.builders;
//
//public class IntegerConditionBuilder {
//    private String fieldName;
//    private AbstractQueryBuilder builder;
//
//    IntegerConditionBuilder(String fieldName, AbstractQueryBuilder builder) {
//        this.fieldName = fieldName;
//        this.builder = builder;
//    }
//
//    public AbstractQueryBuilder equalTo(Integer value) {
//        this.builder.query.equalTo(fieldName, value);
//        return builder;
//    }
//
//    public AbstractQueryBuilder notEqualTo(Integer value) {
//        this.builder.query.notEqualTo(fieldName, value);
//        return builder;
//    }
//
//    public AbstractQueryBuilder between(int value1, int value2) {
//        this.builder.query.between(fieldName, value1, value2);
//        return builder;
//    }
//}
