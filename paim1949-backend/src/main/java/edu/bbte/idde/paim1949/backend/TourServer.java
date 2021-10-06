package edu.bbte.idde.paim1949.backend;

import java.util.ArrayList;
import java.util.List;

public class TourServer {
    private final List<Tour> tours;

    public TourServer() {
        tours = new ArrayList<>();
    }

    public void addTour(Tour tour) {
        tours.add(tour);
    }

    public List<Tour> getTours() {
        return tours;
    }

    public Tour getTour(int index) {
        return tours.get(index);
    }

    public void updateTour(int index, Tour tour) {
        tours.set(index, tour);
    }

    public void deleteTour(int index) {
        tours.remove(index);
    }
}
