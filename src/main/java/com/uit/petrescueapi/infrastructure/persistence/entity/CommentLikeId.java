package com.uit.petrescueapi.infrastructure.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentLikeId implements Serializable {
    private UUID commentId;
    private UUID userId;
}
