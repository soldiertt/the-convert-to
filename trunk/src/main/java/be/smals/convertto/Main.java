package be.smals.convertto;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by soldiertt on 18-07-14.
 */
public class Main {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DIConfiguration.class);
        Application app = context.getBean(Application.class);

        app.getObjects();

        //close the context
        context.close();
    }
}
