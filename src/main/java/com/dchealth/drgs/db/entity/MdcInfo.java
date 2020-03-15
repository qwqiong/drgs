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
public class MdcInfo extends BaseEntity{
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
    @Column(name = "icd_code", columnDefinition = "varchar(50) COMMENT '诊断和手术编码'")
    private String icdCode;
    /**
     * 诊断相关组名称
     */
    @Column(name = "icd_name_cn", columnDefinition = "varchar(50) COMMENT '诊断和手术名称'")
    private String icdNameCn;
}
