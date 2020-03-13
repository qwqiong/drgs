package com.dchealth.drgs.db.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @program: drgs
 * @description: entity基类
 * @author: qiaowenqiong
 * @create: 2020-03-12 12:24
 **/
@Getter
@Setter
@EqualsAndHashCode
@ToString
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
public class BaseEntity implements Serializable {

    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @SequenceGenerator(name = "init_id", initialValue = 10000)
    @Column(columnDefinition = "int(11) COMMENT '主键ID'")
    private Integer id;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(columnDefinition = "dateTime COMMENT '创建时间'")
    // @JsonSerialize(using = LocalDateTimeSerialize.class)
    @JsonDeserialize(as = LocalDateTime.class)
    private LocalDateTime createTime;

    /**
     * 最后修改时间
     */
    @LastModifiedDate
    @Column(columnDefinition = "dateTime COMMENT '最后修改时间'")
    // @JsonSerialize(using = LocalDateTimeSerialize.class)
    @JsonDeserialize(as = LocalDateTime.class)
    private LocalDateTime lastModified;

    /**
     * 是否删除，用于逻辑删除
     */
    @Column(columnDefinition = "bit COMMENT '是否删除，用于逻辑删除'")
    private Boolean remove = false;

}
