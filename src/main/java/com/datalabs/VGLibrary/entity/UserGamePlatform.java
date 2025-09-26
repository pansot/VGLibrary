package com.datalabs.VGLibrary.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_game_platforms")
@IdClass(UserGamePlatformId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserGamePlatform {

    @Id
    @Column(name = "user_id")
    private Integer userId;

    @Id
    @Column(name = "game_id")
    private Integer gameId;

    @Id
    @Column(name = "platform_id")
    private Integer platformId;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "game_id", insertable = false, updatable = false)
    private Game game;

    @ManyToOne
    @JoinColumn(name = "platform_id", insertable = false, updatable = false)
    private Platform platform;

}
