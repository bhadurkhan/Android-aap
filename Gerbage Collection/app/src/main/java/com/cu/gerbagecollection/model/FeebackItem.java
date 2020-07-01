package com.cu.gerbagecollection.model;

public class FeebackItem {
    private String FeedbackId,
            Feedback,
            FeedbackBy,
            CreatedDate;

    public FeebackItem(String feedbackId, String feedback, String feedbackBy, String createdDate) {
        FeedbackId = feedbackId;
        Feedback = feedback;
        FeedbackBy = feedbackBy;
        CreatedDate = createdDate;
    }

    public String getFeedbackId() {
        return FeedbackId;
    }

    public String getFeedback() {
        return Feedback;
    }

    public String getFeedbackBy() {
        return FeedbackBy;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }
}
