package be.smals.convertto;

import be.smals.convertto.model.ConvertTo;
import be.smals.convertto.model.ConvertToSkeleton;
import be.smals.convertto.service.DatabaseService;
import be.smals.convertto.service.FileSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * Created by soldiertt on 18-07-14.
 */
@Component
public class Application {

    private FileSystemService fsService;

    private DatabaseService dbService;

    @Autowired
    public void setFsService(FileSystemService fsService) {
        this.fsService = fsService;
    }

    @Autowired
    public void setDbService(DatabaseService dbService) {
        this.dbService = dbService;
    }

    public void getObjects() {
        try {
            List<ConvertToSkeleton> convertToSkeletons = fsService.readStreamOfLine();
            List<ConvertTo> convertTos = dbService.mapSkeletonToRealObjects(convertToSkeletons);
            fsService.generateJsonFiles(convertTos);
            fsService.generateStaticFiles(convertTos);
            fsService.generateSitemap(convertTos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
