package br.com.hyper.services;

import br.com.hyper.constants.ErrorCodes;
import br.com.hyper.dtos.requests.ReleaseRequestDTO;
import br.com.hyper.dtos.requests.TrackRequestDTO;
import br.com.hyper.dtos.responses.ReleaseResponseDTO;
import br.com.hyper.entities.*;
import br.com.hyper.enums.Privacy;
import br.com.hyper.enums.ReleaseStatus;
import br.com.hyper.enums.ReleaseType;
import br.com.hyper.exceptions.GenericException;
import br.com.hyper.repositories.ArtistRepository;
import br.com.hyper.repositories.LabelRepository;
import br.com.hyper.repositories.ReleaseRepository;
import br.com.hyper.utils.LocalFileStorageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReleaseServiceImpl implements ReleaseService {

    private final ReleaseRepository releaseRepository;
    private final ArtistRepository artistRepository;
    private final LabelRepository labelRepository;
    private final ModelMapper modelMapper;

    @Override
    public ReleaseResponseDTO save(ReleaseRequestDTO releaseDTO, CustomerEntity customer) {
        UUID owner = resolveOwner(customer);
        ArtistEntity artist = getArtist(customer);

        String customerId = owner.toString();
        String upc = generateUpc();
        ReleaseEntity release = createRelease(releaseDTO, owner, upc);

        Path path = Paths.get("releases", upc);

        String coverUrl = (releaseDTO.getCover() == null || releaseDTO.getCover().isEmpty())
                ? customer.getAvatarUrl()
                : LocalFileStorageUtil.saveFile(releaseDTO.getCover(), customerId, path.toString(), "cover");
        release.setCoverUrl(coverUrl);

        List<TrackEntity> tracks = processArtistTracks(customerId, artist, releaseDTO.getTracks(), release);
        release.setTracks(tracks);
        release.setType(tracks.size() > 1 ? releaseDTO.getType() : ReleaseType.SINGLE);

        releaseRepository.save(release);

        customer.getArtistProfile().setFreeTrackLimit(
                customer.getArtistProfile().getFreeTrackLimit() - tracks.size()
        );

        return modelMapper.map(release, ReleaseResponseDTO.class);
    }

    private UUID resolveOwner(CustomerEntity customer) {
        if (Boolean.TRUE.equals(customer.getIsLabel())) {
            return findByIdOrThrowLabelDataNotFoundException(customer.getId()).getId();
        }
        if (Boolean.TRUE.equals(customer.getIsArtist())) {
            return findByIdOrThrowArtistDataNotFoundException(customer.getArtistProfile().getId()).getId();
        }
        throw new GenericException(ErrorCodes.DATA_NOT_FOUND, ErrorCodes.DATA_NOT_FOUND.getMessage());
    }

    private ArtistEntity getArtist(CustomerEntity customer) {
        if (Boolean.TRUE.equals(customer.getIsArtist())) {
            return findByIdOrThrowArtistDataNotFoundException(customer.getArtistProfile().getId());
        }
        throw new GenericException(ErrorCodes.DATA_NOT_FOUND, "Artist profile not found.");
    }

    private ReleaseEntity createRelease(ReleaseRequestDTO dto, UUID owner, String upc) {
        ReleaseEntity release = new ReleaseEntity();
        release.setUpc(upc);
        release.setReleaseDate(ZonedDateTime.now());
        release.setStatus(ReleaseStatus.DRAFT);
        release.setOwner(owner);
        release.setDescription(dto.getDescription());
        release.setTitle(dto.getTracks().getFirst().getTitle());
        return release;
    }

    private List<TrackEntity> processArtistTracks(
            String customerId,
            ArtistEntity artist,
            List<TrackRequestDTO> trackDTOs,
            ReleaseEntity release
    ) {
        List<TrackEntity> savedTracks = new ArrayList<>();
        int remainingPublicSlots = 5 - artistRepository.countPublicTracks(artist.getId(), Privacy.PUBLIC);
        String upc = release.getUpc();

        for (TrackRequestDTO trackDTO : trackDTOs) {
            try {
                MultipartFile audioFile = trackDTO.getFile();
                if (audioFile == null || audioFile.isEmpty()) {
                    log.warn("Arquivo ausente para a faixa '{}'", trackDTO.getTitle());
                    continue;
                }

                if (trackDTO.getTitle() == null || trackDTO.getTitle().isEmpty()) {
                    log.warn("Título ausente para faixa com ID '{}'", trackDTO.getId());
                    continue;
                }

                String sanitizedTitle = sanitize(trackDTO.getTitle());
                String fileUrl = LocalFileStorageUtil.saveFile(audioFile, customerId, "releases/" + upc, sanitizedTitle);

                Path storagePath = Paths.get(System.getProperty("user.dir"), fileUrl.replaceFirst("/", ""));
                AudioFile audio = AudioFileIO.read(storagePath.toFile());

                int duration = audio.getAudioHeader().getTrackLength();

                TrackEntity track = new TrackEntity();
                track.setTitle(trackDTO.getTitle());
                track.setDuration(duration);
                track.setPrice(BigDecimal.valueOf(1.99));
                track.setRelease(release);
                track.setIsrc(generateIsrc());
                track.setFileUrl(fileUrl);
                track.setPlays(BigInteger.ZERO);
                track.setBpm("180"); // TODO: Extract BPM
                track.setKey("7A");   // TODO: Extract Key

                track.setPrivacy(
                        trackDTO.getPrivacy() == Privacy.PUBLIC && remainingPublicSlots-- > 0
                                ? Privacy.PUBLIC
                                : Privacy.PRIVATE
                );

                List<ArtistEntity> artists = resolveTrackArtists(trackDTO, artist);
                track.setArtists(artists);

                savedTracks.add(track);

            } catch (Exception e) {
                log.error("Erro ao processar faixa '{}': {}", trackDTO.getTitle(), e.getMessage());
                throw new GenericException(ErrorCodes.FILE_READ_ERROR, "Erro ao processar faixa: " + trackDTO.getTitle());
            }
        }
        return savedTracks;
    }

    private List<ArtistEntity> resolveTrackArtists(TrackRequestDTO dto, ArtistEntity defaultArtist) {
        if (dto.getArtists() == null || dto.getArtists().isEmpty()) {
            return List.of(defaultArtist);
        }

        List<ArtistEntity> artists = dto.getArtists().stream()
                .map(a -> findByIdOrThrowArtistDataNotFoundException(a.getId()))
                .collect(Collectors.toList());

        if (artists.stream().noneMatch(a -> a.getId().equals(defaultArtist.getId()))) {
            artists.add(defaultArtist);
        }

        return artists;
    }

    private String generateUpc() {
        return "TODO_" + UUID.randomUUID().toString().substring(0, 9).replace("-", "");
    }

    private String generateIsrc() {
        return "BR-KRVO-23-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase() + "TODO";
    }

    private String sanitize(String input) {
        return input.replaceAll("[^a-zA-Z0-9\\s]", "")
                .replaceAll("\\s+", "_")
                .toLowerCase();
    }

    private ArtistEntity findByIdOrThrowArtistDataNotFoundException(UUID id) {
        return artistRepository.findById(id)
                .orElseThrow(() -> new GenericException(ErrorCodes.DATA_NOT_FOUND, "Artist not found with ID: " + id));
    }

    private LabelEntity findByIdOrThrowLabelDataNotFoundException(UUID id) {
        return labelRepository.findById(id)
                .orElseThrow(() -> new GenericException(ErrorCodes.DATA_NOT_FOUND, "Label not found with ID: " + id));
    }
}
