package ua.nure.style.entity;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class Role {

    private long id;
    @NonNull
    private String name;

}
