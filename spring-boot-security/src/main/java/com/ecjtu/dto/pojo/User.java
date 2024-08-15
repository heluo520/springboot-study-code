package com.ecjtu.dto.pojo;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

/**
 * Created with Intellij IDEA.
 *
 * @Author: zws
 * @Date: 2024-08-12
 * @Description:
 */
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ToString
public class User {
    private Long id;
    private String username;
    private String password;
}
