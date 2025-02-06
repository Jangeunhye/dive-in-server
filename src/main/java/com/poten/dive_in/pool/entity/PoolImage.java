package com.poten.dive_in.pool.entity;

import com.poten.dive_in.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter @Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
public class PoolImage extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pool_image_id")
    private Long id;

    private Boolean repImage;

    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pool_id" )
    private Pool pool;
}
