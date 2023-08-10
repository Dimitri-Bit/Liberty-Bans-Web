package me.dimitri.libertyweb.service;

import jakarta.inject.Singleton;
import me.dimitri.libertyweb.model.response.StatisticsResponse;
import me.dimitri.libertyweb.repository.StatisticsRepository;

import java.util.Optional;

import static me.dimitri.libertyweb.utils.TypeConverter.getType;

@Singleton
public class StatisticsService {

    private final StatisticsRepository statisticsRepository;

    public StatisticsService(StatisticsRepository statisticsRepository) {
        this.statisticsRepository = statisticsRepository;
    }

    public StatisticsResponse getStatistics(String type) {
        if (type.equals("all")) {
            return statisticsRepository.query(Optional.empty());
        }
        return statisticsRepository.query(Optional.of(getType(type)));
    }

}
