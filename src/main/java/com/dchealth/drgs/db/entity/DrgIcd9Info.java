package com.dchealth.drgs.db.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DrgIcd9Info extends BaseEntity{
    /**
     * 主要诊断MDC
     */
    @Column(name = "mdc_code", columnDefinition = "varchar(50) COMMENT '主诊断MDC码'")
    private String mdcCode;
    /**
     * 主要诊断DRG
     */
    @Column(name = "drg_code", columnDefinition = "varchar(50) COMMENT '主诊断DRG码'")
    private String drgCode;
    /**
     * 诊断和手术编码
     */
    @Column(name = "icd_code_1", columnDefinition = "varchar(50) COMMENT '诊断和手术编码1'")
    private String icdCode1;
    /**
     * 诊断相关组名称
     */
    @Column(name = "icd_name_cn_1", columnDefinition = "varchar(50) COMMENT '诊断和手术名称1'")
    private String icdNameCn1;
    /**
     * 诊断和手术编码
     */
    @Column(name = "icd_code_2", columnDefinition = "varchar(50) COMMENT '诊断和手术编码2'")
    private String icdCode2;
    /**
     * 诊断相关组名称
     */
    @Column(name = "icd_name_cn_2", columnDefinition = "varchar(50) COMMENT '诊断和手术名称2'")
    private String icdNameCn2;
    /**
     * 诊断相关组名称
     */
    @Column(name = "relation", columnDefinition = "varchar(50) COMMENT '逻辑关系'")
    private String relation;
}
