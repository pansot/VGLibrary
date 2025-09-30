package com.datalabs.VGLibrary.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_games")
@IdClass(UserGameId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserGame {

    @Id
    @Column(name = "user_id")
    private Integer userId;

    @Id
    @Column(name = "game_id")
    private Integer gameId;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "game_id", insertable = false, updatable = false)
    private Game game;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "completion_status", nullable = false)
    private CompletionStatus completionStatus;


}
