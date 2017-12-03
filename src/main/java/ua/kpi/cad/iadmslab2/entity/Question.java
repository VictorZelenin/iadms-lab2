package ua.kpi.cad.iadmslab2.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "question")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "question_id")
    private Integer id;

    @Column(name = "description")
    private String description;

    @Column(name = "complexity")
    private Double complexity;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "question_id")
    private List<QuestionAnswer> answers;
}
