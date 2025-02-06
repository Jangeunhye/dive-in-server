package com.poten.dive_in.auth.entity;

import com.poten.dive_in.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Builder @AllArgsConstructor @NoArgsConstructor
@Entity
public class TokenManager extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_manager_id")
    private Long id;

    private String refreshToken;

    private LocalDateTime expirationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

}
