package com.shikshaspace.shikshaspaceui.service;

import com.shikshaspace.shikshaspaceui.models.Topic;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

/** Service layer for Topic operations */
@Service
public class TopicService {

  public List<Topic> getAllTopics() {
    List<Topic> topics = new ArrayList<>();

    topics.add(
        Topic.builder()
            .id(1L)
            .title("Agentic AI")
            .subtitle("Agentic AI build with RAG + implementation")
            .author("rahul")
            .participantCount(42)
            .category("AI/ML")
            .isActive(true)
            .build());

    topics.add(
        Topic.builder()
            .id(2L)
            .title("Machine Learning")
            .subtitle("ML fundamentals and advanced concepts")
            .author("priya")
            .participantCount(38)
            .category("AI/ML")
            .isActive(true)
            .build());

    topics.add(
        Topic.builder()
            .id(3L)
            .title("Web Development")
            .subtitle("Full-stack development with Spring Boot")
            .author("amit")
            .participantCount(56)
            .category("Web Development")
            .isActive(true)
            .build());

    topics.add(
        Topic.builder()
            .id(4L)
            .title("Data Science")
            .subtitle("Python for data analysis and visualization")
            .author("neha")
            .participantCount(31)
            .category("Data Science")
            .isActive(true)
            .build());

    topics.add(
        Topic.builder()
            .id(5L)
            .title("Cloud Computing")
            .subtitle("AWS, Azure, and GCP fundamentals")
            .author("rajesh")
            .participantCount(28)
            .category("Cloud")
            .isActive(true)
            .build());

    topics.add(
        Topic.builder()
            .id(6L)
            .title("Cybersecurity")
            .subtitle("Ethical hacking and penetration testing")
            .author("sneha")
            .participantCount(19)
            .category("Security")
            .isActive(true)
            .build());

    return topics;
  }

  public Topic getTopicById(Long id) {
    return getAllTopics().stream()
        .filter(topic -> topic.getId().equals(id))
        .findFirst()
        .orElse(null);
  }

  public List<Topic> getTopicsByCategory(String category) {
    return getAllTopics().stream().filter(topic -> category.equals(topic.getCategory())).toList();
  }

  public List<Topic> getActiveTopics() {
    return getAllTopics().stream().filter(Topic::getIsActive).toList();
  }
}
