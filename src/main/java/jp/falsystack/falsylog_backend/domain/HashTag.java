package jp.falsystack.falsylog_backend.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;

@Getter
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = {@UniqueConstraint(name = "unique_name", columnNames = {"name"})})
public class HashTag {

  @JsonBackReference
  @OneToMany(mappedBy = "hashTag", cascade = CascadeType.ALL)
  private final List<PostHashTag> postHashTags = new ArrayList<>();
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "tag_id")
  private Long id;
  private String name;

  @Builder
  public HashTag(String name) {
    this.name = name;
  }

  public void addPostHashTags(List<PostHashTag> postHashTags) {
    this.postHashTags.addAll(postHashTags);
  }
}
