package core;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

/**
 * Created by rishabh.agarwal on 23/05/15.
 */
public class Contacts {
    @JsonProperty
    @NotNull
    String fromId;

    @JsonProperty
    @NotNull
    String toId;

    public String getFromId() {
        return fromId;
    }

    public Contacts setFromId(String fromId) {
        this.fromId = fromId;
        return this;
    }

    public String getToId() {
        return toId;
    }

    public Contacts setToId(String toId) {
        this.toId = toId;
        return this;
    }
}
