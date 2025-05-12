package uz.dev.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.dev.utils.Step;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {

    private Long userId;

    private String firstName;

    private String lastName;

    private String username;

    private List<String> locations;

    private String language;

    private Step step;

    private int currentPage;

}
