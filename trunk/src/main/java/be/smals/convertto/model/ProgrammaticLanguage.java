package be.smals.convertto.model;

/**
 * Created by soldiertt on 18-07-14.
 */
public class ProgrammaticLanguage {

    private Integer id;

    private String label;

    private String shortLabel;

    public ProgrammaticLanguage(Integer id, String label, String shortLabel) {
        this.id = id;
        this.label = label;
        this.shortLabel = shortLabel;
    }

    public Integer getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public String getShortLabel() {
        return shortLabel;
    }

}
