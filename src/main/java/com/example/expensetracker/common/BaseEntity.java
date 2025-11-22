package com.example.expensetracker.common;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public abstract class BaseEntity {
    @Column(name = "deleted")
    protected LocalDateTime deleted;
    @CreationTimestamp
    @Column(name = "created",nullable = false, updatable = false)
    protected LocalDateTime created;
    @UpdateTimestamp
    @Column(name = "updated")
    protected LocalDateTime updated;

    public void markDeleted() {
        this.deleted = LocalDateTime.now();
    }
}
