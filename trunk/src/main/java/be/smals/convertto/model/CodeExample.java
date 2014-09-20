package be.smals.convertto.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by soldiertt on 18-07-14.
 */
public class CodeExample {

    List<String> code;

    public CodeExample() {
        this.code = new ArrayList<>();
    }

    public List<String> getCode() {
        return code;
    }

    public void addLine(String line) {
        this.code.add(line);
    }
}
