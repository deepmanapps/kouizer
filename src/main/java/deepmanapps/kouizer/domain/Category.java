package deepmanapps.kouizer.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "external_id")
    private Long externalId; // Matches OpenTriviaDB IDs (e.g., 9, 10, 11)

    @OneToMany(mappedBy = "category")
    private List<Question> questions = new ArrayList<>();
}