package com.sparta.realtomatoapp.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter // 하위 엔티티에서 수정일을 가져오기 위함.
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
