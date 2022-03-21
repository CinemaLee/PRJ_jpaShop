package com.shop.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@EntityListeners(value = {AuditingEntityListener.class}) // Auditing적용
@MappedSuperclass // 공통 매핑정보가 필요할때 : 부모 클래스를 상속받는 자식 클래스에 매핑 정보만 제공한다.
@Getter
@Setter
public abstract class BaseTimeEntity { // 등록일, 수정일만 필요한 엔티티는 얘만 상속받으면 된다.

    @CreatedDate // 엔티티가 생성되어 저장될때 자동으로 시간을 저장한다.
    @Column(updatable = false)
    private LocalDateTime regTime;

    @LastModifiedDate // 엔티티 값이 변경될때 자동으로 저장.
    private LocalDateTime updateTime;
}
