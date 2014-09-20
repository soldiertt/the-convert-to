package be.smals.convertto.dao;

import be.smals.convertto.model.ConvertTo;
import be.smals.convertto.model.DataType;
import be.smals.convertto.model.ProgrammaticLanguage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by soldiertt on 18-07-14.
 */
@Repository("dbDao")
public class DatabaseDao {

    private DataSource dataSource;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void newDataType(ProgrammaticLanguage lang, String label){

        String sql = "INSERT INTO datatype (lang, label) VALUES (?, ?)";
        Connection conn = null;

        try {
            conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, lang.getId());
            ps.setString(2, label);
            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);

        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {}
            }
        }
    }

    public boolean findConvert(ConvertTo convertTo){

        String sql = "SELECT * FROM convertto WHERE dt_from = ? and dt_to = ?";

        Connection conn = null;

        try {
            conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, convertTo.getFromType().getId());
            ps.setInt(2, convertTo.getToType().getId());
            boolean exists = false;
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                exists = true;
            }
            rs.close();
            ps.close();
            return exists;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {}
            }
        }
    }

    public void newConvertTo(ConvertTo convertTo){

        String sql = "INSERT INTO convertto (dt_from, dt_to) VALUES (?, ?)";
        Connection conn = null;

        try {
            conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, convertTo.getFromType().getId());
            ps.setInt(2, convertTo.getToType().getId());
            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);

        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {}
            }
        }
    }

    public DataType findDataTypeByLangAndLabel(ProgrammaticLanguage lang, String label){

        String sql = "SELECT * FROM datatype WHERE lang = ? and BINARY label = ?";

        Connection conn = null;

        try {
            conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, lang.getId());
            ps.setString(2, label);
            DataType dataType = null;
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                dataType = new DataType(
                        rs.getInt("id"),
                        rs.getString("label"),
                        lang
                );
            }
            rs.close();
            ps.close();
            return dataType;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {}
            }
        }
    }

    public ProgrammaticLanguage findLanguageByShortLabel(String shortLabel){

        String sql = "SELECT * FROM language WHERE BINARY short_label = ?";

        Connection conn = null;

        try {
            conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, shortLabel);
            ProgrammaticLanguage language = null;
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                language = new ProgrammaticLanguage(
                        rs.getInt("id"),
                        rs.getString("label"),
                        rs.getString("short_label")
                );
            }
            rs.close();
            ps.close();
            return language;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {}
            }
        }
    }
}
