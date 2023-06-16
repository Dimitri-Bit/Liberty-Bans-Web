package me.dimitri.liberty.repository;

import io.micronaut.data.annotation.Repository;
import jakarta.inject.Inject;
import me.dimitri.liberty.model.Punishment;
import me.dimitri.liberty.model.PunishmentsResponse;

import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class HistoryRepository {

    private final DataSource dataSource;

    @Inject
    public HistoryRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Transactional
    public PunishmentsResponse queryHistory(char type, int offset) {
        List<Punishment> punishments = new ArrayList<>();
        int pageCount;

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(getHistorySql());
            statement.setString(1, String.valueOf(type));
            statement.setInt(2, offset);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Punishment punishment = mapPunishment(resultSet);
                if (punishment.getEnd() == 0 && punishment.isActive()) {
                    punishment.setLabel("Permanent");
                } else if (!punishment.isActive()) {
                    punishment.setLabel("Expired");
                } else {
                    punishment.setLabel("Active");
                }

                // Temporary fix for Console image
                if (punishment.getOperatorUuid().equals("00000000000000000000000000000000")) {
                    punishment.setOperatorUuid("f78a4d8dd51b4b3998a3230f2de0c670");
                }

                punishments.add(punishment);
            }

            pageCount = queryPageAmount(connection, type);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new PunishmentsResponse(pageCount, punishments);
    }

    public int queryPageAmount(Connection connection, char type) {
        try (PreparedStatement statement = connection.prepareStatement(getPagesSql())) {
            statement.setString(1, String.valueOf(type));
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("row_count");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public String getHistorySql() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("sql/history.sql");
        return new Scanner(inputStream).useDelimiter("\\A").next();
    }

    public String getPagesSql() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("sql/pages.sql");
        return new Scanner(inputStream).useDelimiter("\\A").next();
    }

    public Punishment mapPunishment(ResultSet resultSet) {
        Punishment punishment = new Punishment();
        try {
            punishment.setVictimUuid(byteToString(resultSet.getBytes("victim_uuid")));
            punishment.setOperatorUuid(byteToString(resultSet.getBytes("operator")));
            punishment.setReason(resultSet.getString("reason"));
            punishment.setActive(resultSet.getBoolean("active"));
            punishment.setVictimUsername(resultSet.getString("victim_name"));
            punishment.setOperatorUsername(resultSet.getString("operator_name"));
        } catch (SQLException ignored) {}
        return punishment;
    }

    public String byteToString(byte[] bytes) {
        StringBuilder string = new StringBuilder();

        for (byte b : bytes) {
            string.append(String.format("%02x", b));
        }

        return string.toString();
    }
}
