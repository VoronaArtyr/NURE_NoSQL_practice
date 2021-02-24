package ua.nure.style.entity;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Service {

    private long id;
    @NonNull
    private String name;
    @NonNull
    private String description;
    @NonNull
    private float price;

}
