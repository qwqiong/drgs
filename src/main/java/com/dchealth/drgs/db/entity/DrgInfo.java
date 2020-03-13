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
public class DrgInfo extends BaseEntity{
    /**
     * 诊断相关组码
     */
    @Column(name = "mdc_info", columnDefinition = "varchar(50) COMMENT 'mdc信息'")
    private String mdcInfo;
    /**
     * 诊断相关组码
     */
    @Column(name = "drg_code", columnDefinition = "varchar(50) COMMENT '诊断相关组码'")
    private String drgCode;
    /**
     * 诊断相关组名称
     */
    @Column(name = "drg_name_cn", columnDefinition = "varchar(50) COMMENT '诊断相关组名称'")
    private String drgNameCn;
}
