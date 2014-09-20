package be.smals.convertto.model;

/**
 * Created by soldiertt on 18-07-14.
 */
public class DataType {

    private Integer id;

    private String label;

    private ProgrammaticLanguage lang;

    public DataType(Integer id, String label, ProgrammaticLanguage lang) {
        this.id = id;
        this.label = label;
        this.lang = lang;
    }

    public Integer getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public ProgrammaticLanguage getLang() {
        return lang;
    }

}
