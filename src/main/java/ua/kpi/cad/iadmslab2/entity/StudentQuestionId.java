package ua.kpi.cad.iadmslab2.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Embeddable
public class StudentQuestionId implements Serializable {
    @ManyToOne
    private Student student;

    @ManyToOne
    private Question question;
}
