package be.smals.convertto.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by soldiertt on 18-07-14.
 */
public class ConvertToSkeleton {

    private String languageShortLabel;
    private String dataTypeFrom;
    private String dataTypeTo;

    private List<CodeExample> exampleList;

    public ConvertToSkeleton() {
        exampleList = new ArrayList<>();
    }

    public String getLanguageShortLabel() {
        return languageShortLabel;
    }

    public void setLanguageShortLabel(String languageShortLabel) {
        this.languageShortLabel = languageShortLabel;
    }

    public String getDataTypeFrom() {
        return dataTypeFrom;
    }

    public void setDataTypeFrom(String dataTypeFrom) {
        this.dataTypeFrom = dataTypeFrom;
    }

    public String getDataTypeTo() {
        return dataTypeTo;
    }

    public void setDataTypeTo(String dataTypeTo) {
        this.dataTypeTo = dataTypeTo;
    }

    public List<CodeExample> getExampleList() {
        return exampleList;
    }

    public void addCodeExample(CodeExample codeExample) {
        exampleList.add(codeExample);
    }
}
