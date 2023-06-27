package me.dimitri.liberty.repository;

import jakarta.inject.Inject;
import me.dimitri.liberty.model.Punishment;
import me.dimitri.liberty.model.PunishmentsResponse;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HistoryRepository {

    private final DataSource dataSource;

    private final String HISTORY_SQL;

    @Inject
    public HistoryRepository(DataSource dataSource) {
        this.dataSource = dataSource;
        this.HISTORY_SQL = getHistorySql();
    }

    public PunishmentsResponse query(char type, int offset) {
        try (Connection connection = dataSource.getConnection()) {
            List<Punishment> punishments = new ArrayList<>();
            int pageCount = 0;

            PreparedStatement statement = connection.prepareStatement(HISTORY_SQL);
            statement.setString(1, String.valueOf(type));
            statement.setString(2, String.valueOf(type));
            statement.setInt(3, offset);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Punishment punishment = mapPunishment(resultSet);
                punishments.add(punishment);

                if (resultSet.isLast()) {
                    pageCount = getPageCount(resultSet);
                }
            }
            return new PunishmentsResponse(pageCount, punishments);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getHistorySql() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("sql/history.sql");

        assert inputStream != null;
        return new Scanner(inputStream).useDelimiter("\\A").next();
    }

    public String getLabel(Punishment punishment) {
        if (punishment.getEnd() == 0 && punishment.isActive()) {
            return "Permanent";
        }
        if (!punishment.isActive()) {
            return "Expired";
        }
        return "Active";
    }

    /*
    This is here so that the front end can get an image for the staff avatar since
    the API I'm using for images works on UUIDs
     */
    public void consoleUUIDFix(Punishment punishment) {
        if (punishment.getOperatorUuid().equals("00000000000000000000000000000000")) {
            punishment.setOperatorUuid("f78a4d8dd51b4b3998a3230f2de0c670");
        }
    }

    private Punishment mapPunishment(ResultSet resultSet) throws SQLException {
        Punishment punishment = new Punishment();
        punishment.setVictimUuid(byteToString(resultSet.getBytes("victim_uuid")));
        punishment.setOperatorUuid(byteToString(resultSet.getBytes("operator")));
        punishment.setReason(resultSet.getString("reason"));
        punishment.setActive(resultSet.getBoolean("active"));
        punishment.setVictimUsername(resultSet.getString("victim_name"));
        punishment.setOperatorUsername(resultSet.getString("operator_name"));
        punishment.setStart(resultSet.getLong("start"));
        punishment.setEnd(resultSet.getLong("end"));

        punishment.setLabel(getLabel(punishment));
        consoleUUIDFix(punishment);
        return punishment;
    }

    private int getPageCount(ResultSet resultSet) throws SQLException {
        return resultSet.getInt("row_count");
    }

    private String byteToString(byte[] bytes) {
        StringBuilder string = new StringBuilder();
        for (byte b : bytes) {
            string.append(String.format("%02x", b));
        }
        return string.toString();
    }
}
