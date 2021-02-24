package ua.nure.style.entity;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class Status {

    private String id;
    @NonNull
    private String name;

}
