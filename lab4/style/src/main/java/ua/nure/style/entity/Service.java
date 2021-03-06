package ua.nure.style.entity;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Service {

    private String id;
    @NonNull
    private String name;
    @NonNull
    private String description;
    @NonNull
    private String price;

}
