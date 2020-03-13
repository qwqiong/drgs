package com.dchealth.drgs.db.entity;


import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @Author qiaowenqiong
 * @Date 2020/3/12 上午10:43
 **/
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class DrgIcdR extends BaseEntity {
    /**
     * 主要诊断MDC
     */
    @Column(name = "mdc_info", columnDefinition = "varchar(50) COMMENT '主诊断MDC码'")
    private String mdcInfo;
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
    /**
     * 诊断相关组码
     */
    @Column(name = "drg_code", columnDefinition = "varchar(50) COMMENT '诊断相关组码'")
    private String drgCode;
    /**
     * 诊断相关组码
     */
    @Column(name = "drg_name_cn", columnDefinition = "varchar(50) COMMENT '诊断相关组名称'")
    private String drgNameCn;
}
