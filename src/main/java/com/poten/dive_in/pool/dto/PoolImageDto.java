package com.poten.dive_in.pool.dto;

import com.poten.dive_in.pool.entity.PoolImage;
import lombok.Builder;
import lombok.Getter;

@Getter @Builder
public class PoolImageDto {

    private Boolean repImage;

    private String imageUrl;

    public static PoolImageDto ofEntity(PoolImage poolImage){
        return PoolImageDto.builder()
                .repImage(poolImage.getRepImage())
                .imageUrl(poolImage.getImageUrl())
                .build();

    }
}
