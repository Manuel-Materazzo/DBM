package com.DeBM.ApiDeBM.dto;

import lombok.*;
import org.springframework.lang.Nullable;

@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class FileDTO {
    String path;
    @Nullable Integer length;
}
