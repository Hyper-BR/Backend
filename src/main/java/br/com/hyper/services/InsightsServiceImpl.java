package br.com.hyper.services;

import br.com.hyper.dtos.InsightsDTO;
import br.com.hyper.dtos.TrackInsightsDTO;
import br.com.hyper.entities.CustomerEntity;
import br.com.hyper.repositories.PlayHistoryRepository;
import br.com.hyper.repositories.TrackDownloadRepository;
import br.com.hyper.repositories.TrackPurchaseRepository;
import br.com.hyper.repositories.TrackRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class InsightsServiceImpl implements InsightsService {

    private final PlayHistoryRepository playHistoryRepository;
    private final TrackPurchaseRepository trackPurchaseRepository;
    private final TrackDownloadRepository trackDownloadRepository;
    private final TrackRepository trackRepository;
    private ModelMapper modelMapper;

    @Override
    public InsightsDTO getInsights(CustomerEntity customer) {
        UUID artistId = customer.getId();

        BigInteger totalPlays = playHistoryRepository.countByTrackArtistId(artistId);
        BigInteger totalPurchases = trackPurchaseRepository.countByArtistId(artistId);
        BigInteger totalDownloads = trackDownloadRepository.countByTrackArtistId(artistId);
        BigDecimal totalRevenue = trackPurchaseRepository.sumRevenueByArtistId(artistId);

        List<TrackInsightsDTO> tracks = trackRepository.findByArtistId(artistId, Pageable.unpaged()).stream()
                .map(track -> {
                    UUID trackId = track.getId();
                    String title = track.getTitle();
                    BigInteger plays = playHistoryRepository.countByTrackArtistIdAndTrackId(artistId, trackId);
                    BigInteger purchases = trackPurchaseRepository.countByArtistIdAndTrackId(artistId, trackId);
                    BigInteger downloads = trackDownloadRepository.countByTrackArtistIdAndTrackId(artistId, trackId);
                    BigDecimal revenue = trackPurchaseRepository.sumRevenueByArtistIdAndTrackId(artistId, trackId);
                    return new TrackInsightsDTO(trackId, title, plays, purchases, downloads, revenue);
                })
                .sorted(Comparator.comparing(TrackInsightsDTO::getPlays).reversed())
                .limit(5)
                .toList();

        return InsightsDTO.builder()
                .totalPlays(totalPlays)
                .totalPurchases(totalPurchases)
                .totalDownloads(totalDownloads)
                .totalRevenue(totalRevenue)
                .tracks(tracks)
                .build();
    }
}
