package br.com.hyper.services;

import br.com.hyper.dtos.InsightsDTO;
import br.com.hyper.dtos.ListenerDTO;
import br.com.hyper.dtos.responses.ArtistResponseDTO;
import br.com.hyper.dtos.responses.TrackResponseDTO;
import br.com.hyper.entities.TrackEntity;
import br.com.hyper.entities.CustomerEntity;
import br.com.hyper.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class InsightsServiceImpl implements InsightsService {

    private final PlayHistoryRepository playHistoryRepository;
    private final TrackRepository trackRepository;
    private final TrackPurchaseRepository trackPurchaseRepository;
    private final TrackDownloadRepository trackDownloadRepository;
    private final ModelMapper modelMapper;

    @Override
    public InsightsDTO getInsights(CustomerEntity customer) {
        UUID artistId = customer.getId();

        List<TrackEntity> trackEntities = trackRepository.findTracksByArtistId(artistId);

        List<TrackResponseDTO> tracks = trackEntities.stream().map(track -> {
            TrackResponseDTO dto = modelMapper.map(track, TrackResponseDTO.class);

            dto.setArtists(track.getArtists().stream()
                    .map(artist -> modelMapper.map(artist, ArtistResponseDTO.class))
                    .toList());

            dto.setPlays(playHistoryRepository.countDistinctByTrackId(track.getId()));
            dto.setPurchases(trackPurchaseRepository.countDistinctByTrackId(track.getId()));
            dto.setDownloads(trackDownloadRepository.countDistinctByTrackId(track.getId()));
            dto.setPrice(trackPurchaseRepository.sumPriceByTrackId(track.getId()));

            if (dto.getPlays() == null) dto.setPlays(0L);
            if (dto.getPurchases() == null) dto.setPurchases(0L);
            if (dto.getDownloads() == null) dto.setDownloads(0L);
            if (dto.getPrice() == null) dto.setPrice(BigDecimal.ZERO);

            return dto;
        }).toList();

        Long totalPlays = tracks.stream().mapToLong(TrackResponseDTO::getPlays).sum();
        Long totalPurchases = tracks.stream().mapToLong(TrackResponseDTO::getPurchases).sum();
        Long totalDownloads = tracks.stream().mapToLong(TrackResponseDTO::getDownloads).sum();
        BigDecimal totalRevenue = tracks.stream()
                .map(TrackResponseDTO::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<ListenerDTO> projections = playHistoryRepository.findTopListenersWithNameByArtistId(artistId);

        List<ListenerDTO> topListeners = projections.stream()
                .map(p -> new ListenerDTO(p.getId(), p.getName(), p.getPlays()))
                .toList();

        return InsightsDTO.builder()
                .totalPlays(totalPlays)
                .totalPurchases(totalPurchases)
                .totalDownloads(totalDownloads)
                .totalRevenue(totalRevenue)
                .totalAlbums(null)
                .totalTracks(tracks.size())
                .tracks(tracks)
                .topListeners(topListeners)
                .build();
    }
}
