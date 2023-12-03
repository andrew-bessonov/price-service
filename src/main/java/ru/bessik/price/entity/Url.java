package ru.bessik.price.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@Table("url")
@NoArgsConstructor
@AllArgsConstructor
public class Url {

    @Id
    private Long id;

    private String productUrl;

    private Boolean isNeedUpdate;
}
