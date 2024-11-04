package com.sparta.realtomatoapp.common.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class ModifiedAuditingEntity extends BaseAuditingEntity {

    @LastModifiedDate
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime modifiedAt;


    public ModifiedAuditingEntity() {
        super();
    }
}
