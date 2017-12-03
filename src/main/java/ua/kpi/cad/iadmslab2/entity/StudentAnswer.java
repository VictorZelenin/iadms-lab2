package ua.kpi.cad.iadmslab2.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "student_question")
@AssociationOverrides({
        @AssociationOverride(name = "pk.student", joinColumns = @JoinColumn(name = "student_id")),
        @AssociationOverride(name = "pk.question", joinColumns = @JoinColumn(name = "question_id"))
})
public class StudentAnswer implements Serializable {
    @EmbeddedId
    private StudentQuestionId pk = new StudentQuestionId();

    @Column(name = "temp_complexity")
    private Double questionComplexity;

    @Column(name = "answer_value")
    private Double answerValue;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "answer_time")
    private Date answerTime;

    @Transient
    public Student getStudent() {
        return getPk().getStudent();
    }

    public void setStudent(Student student) {
        getPk().setStudent(student);
    }

    @Transient
    public Question getQuestion() {
        return getPk().getQuestion();
    }

    public void setQuestion(Question question) {
        getPk().setQuestion(question);
    }
}
