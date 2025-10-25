package com.shikshaspace.shikshaspaceui.models;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Topic domain model Represents a learning topic in ShikshaSpace
 *
 * @author ShikshaSpace Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Topic {

  private Long id;

  private String title;

  private String subtitle;

  private String author;

  @Builder.Default private LocalDateTime createdAt = LocalDateTime.now();

  private Integer participantCount;

  private String category;

  private Boolean isActive;
}
