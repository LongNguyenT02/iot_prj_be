package com.filemanage.iot_prj_be.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaginationResponse {
    private List<?> content;
    private int count;
    private int page;
    private int perPage;
}
