package com.grupa2.przewodnikturystyczny;

/**
 * Created by KPC on 2017-03-01.
 */
public class AttractionLoadedEvent {
    private Attraction[] mAttractions;

    public AttractionLoadedEvent(Attraction[] attractions) {
        mAttractions = attractions;
    }

    public Attraction[] getAttractions() {
        return  mAttractions;
    }



}
