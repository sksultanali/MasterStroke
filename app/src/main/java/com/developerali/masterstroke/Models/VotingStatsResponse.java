package com.developerali.masterstroke.Models;

import java.util.List;

public class VotingStatsResponse {

    private String status;
    private Stats stats;

    // Default constructor
    public VotingStatsResponse() {
    }

    // Parameterized constructor
    public VotingStatsResponse(String status, Stats stats) {
        this.status = status;
        this.stats = stats;
    }

    // Getters and Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Stats getStats() {
        return stats;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }

    @Override
    public String toString() {
        return "VotingStatsResponse{" +
                "status='" + status + '\'' +
                ", stats=" + stats +
                '}';
    }

    // Inner class for "stats"
    public static class Stats {
        private String total_voters;
        private VoteCount vote_done;
        private VoteCount our_party;
        private VoteCount opposition_party;
        private VoteCount doubtful;

        // Default constructor
        public Stats() {
        }

        // Getters and Setters
        public String getTotal_voters() {
            return total_voters;
        }

        public void setTotal_voters(String total_voters) {
            this.total_voters = total_voters;
        }

        public VoteCount getVote_done() {
            return vote_done;
        }

        public void setVote_done(VoteCount vote_done) {
            this.vote_done = vote_done;
        }

        public VoteCount getOur_party() {
            return our_party;
        }

        public void setOur_party(VoteCount our_party) {
            this.our_party = our_party;
        }

        public VoteCount getOpposition_party() {
            return opposition_party;
        }

        public void setOpposition_party(VoteCount opposition_party) {
            this.opposition_party = opposition_party;
        }

        public VoteCount getDoubtful() {
            return doubtful;
        }

        public void setDoubtful(VoteCount doubtful) {
            this.doubtful = doubtful;
        }

        @Override
        public String toString() {
            return "Stats{" +
                    "total_voters='" + total_voters + '\'' +
                    ", vote_done=" + vote_done +
                    ", our_party=" + our_party +
                    ", opposition_party=" + opposition_party +
                    ", doubtful=" + doubtful +
                    '}';
        }
    }

    // Inner class for count + percentage objects
    public static class VoteCount {
        private String count;
        private double percentage;  // or int if percentage is always whole number

        // Default constructor
        public VoteCount() {
        }

        public VoteCount(String count, double percentage) {
            this.count = count;
            this.percentage = percentage;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public double getPercentage() {
            return percentage;
        }

        public void setPercentage(double percentage) {
            this.percentage = percentage;
        }

        @Override
        public String toString() {
            return "VoteCount{" +
                    "count='" + count + '\'' +
                    ", percentage=" + percentage +
                    '}';
        }
    }
}