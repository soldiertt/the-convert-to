package be.smals.convertto.service;

import be.smals.convertto.dao.DatabaseDao;
import be.smals.convertto.model.ConvertTo;
import be.smals.convertto.model.ConvertToSkeleton;
import be.smals.convertto.model.DataType;
import be.smals.convertto.model.ProgrammaticLanguage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by soldiertt on 18-07-14.
 */
@Service("dbService")
public class DatabaseService {

    private DatabaseDao dbDao;

    @Autowired
    public void setDbDao(DatabaseDao dbDao) {
        this.dbDao = dbDao;
    }

    public List<ConvertTo> mapSkeletonToRealObjects(List<ConvertToSkeleton> convertToSkeletons) {
        List<ConvertTo> convertTos = new ArrayList<>();
        for (ConvertToSkeleton skeleton : convertToSkeletons) {
            ConvertTo convertTo = new ConvertTo();
            ProgrammaticLanguage lang = dbDao.findLanguageByShortLabel(skeleton.getLanguageShortLabel());
            DataType dataTypeFrom = dbDao.findDataTypeByLangAndLabel(lang, skeleton.getDataTypeFrom());
            System.out.println("from : " + dataTypeFrom);
            if (dataTypeFrom == null) {
                dbDao.newDataType(lang, skeleton.getDataTypeFrom());
                dataTypeFrom = dbDao.findDataTypeByLangAndLabel(lang, skeleton.getDataTypeFrom());
            }
            DataType dataTypeTo = dbDao.findDataTypeByLangAndLabel(lang, skeleton.getDataTypeTo());
            System.out.println("to : " + dataTypeTo);
            if (dataTypeTo == null) {
                dbDao.newDataType(lang, skeleton.getDataTypeTo());
                dataTypeTo = dbDao.findDataTypeByLangAndLabel(lang, skeleton.getDataTypeTo());
            }
            convertTo.setFromType(dataTypeFrom);
            convertTo.setToType(dataTypeTo);
            convertTo.setExampleList(skeleton.getExampleList());
            //Add to convert table
            if (!dbDao.findConvert(convertTo)) {
                dbDao.newConvertTo(convertTo);
            }
            convertTos.add(convertTo);
        }
        return convertTos;
    }
}
