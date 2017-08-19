package hu.bets.apigateway.model.users;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class User {

    private final String id;
    private final String pictureUrl;
    private final String name;
    private String token;

    @JsonCreator
    public User(@JsonProperty("id") String id,
                @JsonProperty("pictureUrl") String pictureUrl,
                @JsonProperty("name") String name) {
        this.id = id;
        this.pictureUrl = pictureUrl;
        this.name = name;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", pictureUrl='" + pictureUrl + '\'' +
                ", name='" + name + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
