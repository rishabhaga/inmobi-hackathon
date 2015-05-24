package core;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

/**
 * Created by rishabh.agarwal on 23/05/15.
 */
public class User {
    @JsonProperty
    @NotNull
    private String id;

    @NotNull
    @JsonProperty
    private String uuid;

    @NotNull
    @JsonProperty
    private String deviceId;

    public String getId() {
        return id;
    }

    public User setId(String id) {
        this.id = id;
        return this;
    }

    public User() {}

    public String getUuid() {
        return uuid;
    }

    public User setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public User setDeviceId(String deviceId) {
        this.deviceId = deviceId;
        return this;
    }

}



