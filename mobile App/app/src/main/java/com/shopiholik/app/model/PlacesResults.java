package com.shopiholik.app.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author agrawroh
 * @version v1.0
 */
public class PlacesResults {
    @SerializedName("candidates")
    private List<Candidate> candidates;

    public List<Candidate> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<Candidate> candidates) {
        this.candidates = candidates;
    }

    public final class Candidate {
        @SerializedName("name")
        private String name;

        @SerializedName("formatted_address")
        private String formattedAddress;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getFormattedAddress() {
            return formattedAddress;
        }

        public void setFormattedAddress(String formattedAddress) {
            this.formattedAddress = formattedAddress;
        }
    }
}