package com.syy.tessystem.entities;

import lombok.*;
import java.sql.Timestamp;
import java.util.List;

/**
 * create by: syy
 * description: TODO
 * create time: 2022/4/30 14:49
 *
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaperAndQuestion {

    private Integer id;

    private String paperName;

    private Integer maxMark;

    private Integer creatorId;

    private Integer courseId;

    private Timestamp createTime;

    private Integer change;

    private List<QuestionPublicSc> list;

}
