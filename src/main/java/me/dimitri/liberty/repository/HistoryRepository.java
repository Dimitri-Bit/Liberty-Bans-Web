package me.dimitri.liberty.repository;

import io.micronaut.cache.annotation.Cacheable;
import io.micronaut.data.annotation.Repository;
import jakarta.inject.Inject;
import me.dimitri.liberty.model.Punishment;

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
    public List<Punishment> queryHistory(char type, int offset) {
        List<Punishment> punishments = new ArrayList<>();
        HashSet<String> uuids = new HashSet<>();

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(historySql());
            statement.setString(1, String.valueOf(type));
            statement.setInt(2, offset);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Punishment punishment = mapPunishment(resultSet);
                String operatorUuid = punishment.getOperatorUuid();

                if (!operatorUuid.equals("00000000000000000000000000000000")) { // Check if operator is console
                    uuids.add(operatorUuid);
                }
                uuids.add(punishment.getVictimUuid());
                punishments.add(punishment);
            }

            HashMap<String, String> names = queryNames(uuids, connection);
            updatePunishmentUsernames(punishments, names);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return punishments;
    }

    private HashMap<String, String> queryNames(HashSet<String> UUIDs, Connection connection) {
        HashMap<String, String> names = new HashMap<>();
        if (UUIDs.isEmpty()) {
            return names;
        }

        String sql = constructNameSql(UUIDs);
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String UUID = byteToString(resultSet.getBytes("uuid"));
                String name = resultSet.getString("name");
                names.put(UUID, name);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return names;
    }

    private void updatePunishmentUsernames(List<Punishment> punishments, Map<String, String> names) {
        for (Punishment punishment : punishments) {
            punishment.setVictimUsername(names.getOrDefault(punishment.getVictimUuid(), "Unknown"));
            punishment.setOperatorUsername(names.getOrDefault(punishment.getOperatorUuid(), "Console"));
        }
    }

    public String historySql() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("sql/history.sql");
        return new Scanner(inputStream).useDelimiter("\\A").next();
    }

    public String constructNameSql(HashSet<String> UUIDs) {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("sql/names.sql");
        String mainSql = new Scanner(inputStream).useDelimiter("\\A").next();

        StringBuilder sql = new StringBuilder(mainSql);
        sql.replace(sql.indexOf("?"), sql.indexOf("?") + 1, constructUuidList(UUIDs));

        return sql.toString();
    }

    private String constructUuidList(HashSet<String> UUIDs) {
        StringBuilder uuidList = new StringBuilder();

        for (String UUID : UUIDs) {
            uuidList.append("UNHEX('").append(UUID).append("'),");
        }

        uuidList.deleteCharAt(uuidList.length() - 1); // delete the extra "," at the end

        return uuidList.toString();
    }

    public static Punishment mapPunishment(ResultSet resultSet) {
        Punishment punishment = new Punishment();
        try {
            punishment.setVictimUuid(byteToString(resultSet.getBytes("victim_uuid")));
            punishment.setOperatorUuid(byteToString(resultSet.getBytes("operator")));
            punishment.setReason(resultSet.getString("reason"));
        } catch (SQLException ignored) {}
        return punishment;
    }

    public static String byteToString(byte[] bytes) {
        StringBuilder string = new StringBuilder();

        for (byte b : bytes) {
            string.append(String.format("%02x", b));
        }

        return string.toString();
    }

}
