package be.smals.convertto.model;

import java.util.List;

/**
 * Created by soldiertt on 18-07-14.
 */
public class ConvertTo {

    private DataType fromType;

    private DataType toType;

    private List<CodeExample> exampleList;

    public DataType getFromType() {
        return fromType;
    }

    public void setFromType(DataType fromType) {
        this.fromType = fromType;
    }

    public DataType getToType() {
        return toType;
    }

    public void setToType(DataType toType) {
        this.toType = toType;
    }

    public List<CodeExample> getExampleList() {
        return exampleList;
    }

    public void setExampleList(List<CodeExample> exampleList) {
        this.exampleList = exampleList;
    }
}
