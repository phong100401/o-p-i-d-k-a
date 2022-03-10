package com.example.orderservice.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@MappedSuperclass // để kế thừa các thuộc tính từ một lớp cơ sở
@EntityListeners({AuditingEntityListener.class})// lắng nghe cái entity của mình trước và sau mình làm một cái gì đó và động đên database với cái entity tương ứng
public abstract class BaseEntity implements Serializable {
    private LocalDate createdAt;
    private LocalDate updateAt;
    private LocalDate deleteAt;
}
