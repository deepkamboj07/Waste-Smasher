package com.practice.wastemanagment;

import java.util.ArrayList;

public class ModelNews2 {
    private String status,totalResults;
    private ArrayList<ModelClassNews> results;

    public ModelNews2(String status, String totalResults, ArrayList<ModelClassNews> results) {
        this.status = status;
        this.totalResults = totalResults;
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(String totalResults) {
        this.totalResults = totalResults;
    }

    public ArrayList<ModelClassNews> getResults() {
        return results;
    }

    public void setResults(ArrayList<ModelClassNews> results) {
        this.results = results;
    }
}
