package com.test.webfluxperformance.user;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class User {
    String id;
    String name;
    String hobby;
}
