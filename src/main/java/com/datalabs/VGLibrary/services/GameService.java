package com.datalabs.VGLibrary.services;

import com.datalabs.VGLibrary.domain.CreateGameRequest;
import com.datalabs.VGLibrary.entity.Game;
import com.datalabs.VGLibrary.entity.Genre;
import com.datalabs.VGLibrary.repository.GameRepository;
import com.datalabs.VGLibrary.repository.GenreRepository;
import com.datalabs.VGLibrary.repository.PlatformRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GameService {
    @Autowired
    GameRepository gameRepository;

    @Autowired
    GenreRepository genreRepository;

    @Autowired
    PlatformRepository platformRepository;

    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    public Game createGame(CreateGameRequest request) {
        Game game = new Game();
        game.setTitle(request.getTitle());
        game.setDeveloper(request.getDeveloper());
        game.setPublisher(request.getPublisher());
        game.setReleaseYear(request.getReleaseYear());

        Set<Genre> genres = new HashSet<>();
        for(Integer genreId : request.getGenreIds()) {
            Genre genre = genreRepository.findById(genreId).orElse(null);
            if (genre != null) {
                genres.add(genre);
            }
        }
        game.setGenres(genres);

        return gameRepository.save(game);
    }
}
