package com.brainydroid.daydreaming.db;

import com.brainydroid.daydreaming.background.Logger;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.ArrayList;

public class ManySlidersQuestionDescriptionDetails implements IQuestionDescriptionDetails {

    private static String TAG = "ManySlidersQuestionDescriptionDetails";

    @JsonView(Views.Internal.class)
    private String type = "ManySliders";
    @JsonView(Views.Internal.class)
    private String text = null;
    @JsonView(Views.Internal.class)
    private ArrayList<String> availableSliders = null;
    @JsonView(Views.Internal.class)
    private ArrayList<String> defaultSliders = null;
    @JsonView(Views.Internal.class)
    private ArrayList<String> hints = null;

    @Override
    public synchronized String getType() {
        return type;
    }

    public synchronized String getText() {
        return text;
    }

    public synchronized ArrayList<String> getAvailableSliders() {
        return availableSliders;
    }

    public synchronized ArrayList<String> getDefaultSliders() {
        return defaultSliders;
    }

    public synchronized ArrayList<String> getHints() {
        return hints;
    }

    public synchronized void validateInitialization() throws JsonParametersException {
        Logger.v(TAG, "Validating question details");

        if (text == null) {
            throw new JsonParametersException("text in " +
                    "ManySlidersQuestionDescriptionDetails can't be null");
        }

        if (availableSliders == null || availableSliders.size() == 0) {
            throw new JsonParametersException("availableSliders in " +
                    "ManySlidersQuestionDescriptionDetails can't by null or empty");
        }

        if (defaultSliders == null || defaultSliders.size() == 0) {
            throw new JsonParametersException("defaultSliders in " +
                    "ManySlidersQuestionDescriptionDetails can't by null or empty");
        }

        for (String slider : defaultSliders) {
            if (!availableSliders.contains(slider)) {
                throw new JsonParametersException("All sliders in defaultSliders must be defined " +
                        "in availableSliders too");
            }
        }

        if (hints == null || hints.size() < 2) {
            throw new JsonParametersException("There must be at least two hints in " +
                    "ManySlidersQuestionDescriptionDetails");
        }
    }
}
